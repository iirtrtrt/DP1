package org.springframework.samples.parchisoca.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserRepository;
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







    @Transient
    private static final Logger logger = LogManager.getLogger(GameService.class);



    @Autowired
    public GameService(GameRepository gameRepository, GameBoardRepository gameBoardRepository, GamePieceRepository gamePieceRepository
                        , UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gamePieceRepository = gamePieceRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.userRepository = userRepository;

    }

    @Transactional
    public void initGame(Game game) throws DataAccessException {
        game.setStatus(GameStatus.CREATED);
        logger.info("setting created");
        game.setStartTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        logger.info("set time");
        gameRepository.save(game);
        logger.info("save");

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
            if (game.getOther_players().contains(user) && game.getStatus().equals(GameStatus.CREATED))
                return true;
        }
        return false;
    }

    public boolean gameNameExists(Game game_find) {
        // Optional<Game> gameOptional =
        // this.gameRepository.findByName(game_find.getName());
        logger.info("gameNameExists");
        return this.gameRepository.existsByName(game_find.getName());
        // return gameOptional.filter(game ->
        // (game.getName().equals(game_find.getName()) &&
        // game.getStatus().equals(GameStatus.CREATED))).isPresent();
    }

    public void deleteAllGamePieces(Game game) {
        List<User> user_list = game.getOther_players();
        user_list.addAll(game.getCurrent_players());
        logger.info("user_list size: " + user_list.size());

        for (User user : user_list) {
            user.deleteAllGamePieces();
            userRepository.save(user);
        }
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
