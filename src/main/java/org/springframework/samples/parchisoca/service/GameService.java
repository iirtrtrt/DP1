package org.springframework.samples.parchisoca.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.model.game.Turns;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.GameBoard;
import org.springframework.samples.parchisoca.model.game.GamePiece;
import org.springframework.samples.parchisoca.repository.GameBoardRepository;
import org.springframework.samples.parchisoca.repository.GamePieceRepository;
import org.springframework.samples.parchisoca.repository.GameRepository;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.repository.UserRepository;
import org.springframework.samples.parchisoca.model.user.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Transient;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameBoardRepository gameBoardRepository;

    @Autowired
    private GamePieceRepository gamePieceRepository;

    @Autowired
    private UserService userService;


    @Transient
    private static final Logger logger = LogManager.getLogger(GameService.class);



    @Autowired
    public GameService(GameRepository gameRepository, GameBoardRepository gameBoardRepository, GamePieceRepository gamePieceRepository
                        , UserRepository userRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.gamePieceRepository = gamePieceRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.userRepository = userRepository;
        this.userService = userService;

    }

    @Transactional
    public void initGame(Game game) throws DataAccessException {
        game.setStatus(GameStatus.CREATED);
        game.setStartTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        gameRepository.save(game);


    }

    /**
     * saves a game to the database
     */
    @Transactional
    public void saveGame(Game game) throws DataAccessException {
        gameRepository.save(game);
    }

    @Transactional
    public void saveGames(List<Game> games) throws DataAccessException {
        for (Game game : games) {
            game.setStatus(GameStatus.CREATED);
            game.setStartTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }
        gameRepository.saveAll(games);
    }

    @Transactional
    public void saveGameBoard(GameBoard gameBoard, Game game) throws DataAccessException {
        gameBoard.setGame(game);
        gameBoardRepository.save(gameBoard);
    }

    public List<Game> findGameByStatus(GameStatus status) throws DataAccessException {
        return gameRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Optional<Game> findGamebyID(Integer id) throws DataAccessException {
        return gameRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Game> findGameByName(String name) throws DataAccessException {
        return gameRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Game> findAllGames() {
        List<Game> games = new ArrayList<>();
        gameRepository.findAll().forEach(games::add);
        return games;
    }

    @Transactional
    public List<GamePiece> createGamePieces(User user, Game game, Color color) {
        List<GamePiece> gamePieces = new ArrayList<>();
        if (game.getType() == GameType.Parchis) {
            for (int i = 0; i < 4; i++) {
                GamePiece parchis_piece = new GamePiece();
                parchis_piece.setTokenColor(color);
                parchis_piece.setUser_id(user);
                gamePieces.add(parchis_piece);
                this.gamePieceRepository.save(parchis_piece);
                user.setGamePieces(gamePieces);
            }
        } else {
            GamePiece oca_piece = new GamePiece();
            oca_piece.setTokenColor(color);
            oca_piece.setUser_id(user);
            gamePieces.add(oca_piece);
            this.gamePieceRepository.save(oca_piece);
            user.setGamePieces(gamePieces);
        }
        return gamePieces;
    }

    public boolean checkUserAlreadyinGame(User user) {
        List<Game> all_games = new ArrayList<>();
        this.gameRepository.findAll().forEach(all_games::add);
        for (Game game : all_games) {
            if (game.getOther_players().contains(user) && game.getStatus() == GameStatus.CREATED)
                return true;
        }
        return false;
    }

    public boolean gameNameExists(Game game_find) {
        return this.gameRepository.existsByName(game_find.getName());
    }

    public void deleteAllGamePieces(Game game) {
        List<User> user_list = new ArrayList<>();
        user_list.addAll(game.getCurrent_players());
        logger.info("user_list size: " + user_list.size());

        for (User user : user_list) {
            user.deleteAllGamePieces();
            user.setTokenColor(null);
            userRepository.save(user);
        }
    }



    public void setPlayersOfGame(Game game, User user)
    {
        game.setCreator(user);
        game.setCurrent_players(user);
        game.setCurrent_player(user);
        user.addCreatedGame(game);

    }

    public void quitGame(int gameid)
    {
        Optional<Game> gameOptional = this.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);


        game.setStatus(GameStatus.FINISHED);
        deleteAllGamePieces(game);
        game.setEndTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        if(game.getWinner() != null && game.getWinner().getRole() == UserRole.AI){
            game.setWinner(null);
        }
        deleteTurns(game);
        if(game.isAI()){
           deleteAI(game);
        }
        saveGame(game);
    }

    private void deleteTurns(Game game){
        for(Turns turn : game.getTurns()){
            turn.setUser_id(null);
        }
    }



    private void deleteAI(Game game) {
        User ai = new User();
        for(User user : game.getCurrent_players()){
            if(user.getRole() == UserRole.AI){
                user.getPlayed_games().remove(game);
                game.getOther_players().remove(user);
                game.getCurrent_players().remove(user);
                saveGame(game);
                ai = user;
                break;
            }
        }
        userService.saveUser(ai, UserRole.AI);
        this.userService.deleteUser(ai.getUsername());

    }

    public boolean checkColor(Game game, Color color)
    {
        for (User user: game.getCurrent_players()) {
            if (user.getGamePieces().get(0).getTokenColor().getRGB() == color.getRGB())
                return false;
        }

        return true;
    }
}
