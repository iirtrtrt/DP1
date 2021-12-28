package org.springframework.samples.parchisoca.game;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private GameBoardRepository gameBoardRepository;

    @Autowired
    private GamePieceRepository gamePieceRepository;

    @Autowired
    private TurnsRepository turnsRepository;




    @Autowired
    public GameService(GameRepository gameRepository, GameBoardRepository gameBoardRepository, GamePieceRepository gamePieceRepository
                        ,TurnsRepository turnsRepo) {
        this.gameRepository = gameRepository;
        this.gamePieceRepository = gamePieceRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.turnsRepository = turnsRepo;
    }


    /**
     * saves a game to the database
     */
    @Transactional
    public void saveGame(Game game) throws DataAccessException {
        game.setStatus(GameStatus.CREATED);
        game.setStartTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        gameRepository.save(game);
    }


   @Transactional
    public void saveGames(List<Game> games) throws DataAccessException {
        for(Game game : games) {
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

    public List < Game > findGameByStatus(GameStatus status) throws DataAccessException {
        return gameRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Optional < Game > findGamebyID(Integer id) throws DataAccessException {
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
    public List < GamePiece > createGamePieces(User user, Game game, Color color) {
        List < GamePiece > gamePieces = new ArrayList < > ();
        if (game.getType() == GameType.Parchis) {
            for (int i = 0; i < 4; i++) {
                GamePiece parchis_piece = new GamePiece();
                parchis_piece.setTokenColor(color);
                parchis_piece.setUser_id(user);
                gamePieces.add(parchis_piece);
                this.gamePieceRepository.save(parchis_piece);
                user.setGamePieces(gamePieces);
            }
        }
        else
        {
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
        List < Game > all_games = new ArrayList < > ();
        this.gameRepository.findAll().forEach(all_games::add);
        for (Game game: all_games) {
            if (game.getOther_players().contains(user))
                return true;
        }
        return false;
    }

    public boolean gameNameExists(Game game) {
        return this.gameRepository.existsByName(game.getName());
    }

}
