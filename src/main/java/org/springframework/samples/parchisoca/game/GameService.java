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
    public GameService(GameRepository gameRepository, GameBoardRepository gameBoardRepository, GamePieceRepository gamePieceRepository){
        this.gameRepository = gameRepository;
        this.gamePieceRepository = gamePieceRepository;
        this.gameBoardRepository = gameBoardRepository;
    }


    /**
     * saves a game to the database
     */
    @Transactional
    public void saveGame(Game game) throws DataAccessException {
        game.setStatus(GameStatus.CREATED);
        game.setStartTime(LocalDateTime.now());
        gameRepository.save(game);
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
    public List<Game> findAllGames() {
        List<Game> games = new ArrayList<>();
        gameRepository.findAll().forEach(games::add);
        return games;
    }

    @Transactional
    public List<GamePiece> createGamePieces(User user, Game game, Color color)
    {
        List<GamePiece> gamePieces = new ArrayList<>();
        if( game.getType() == GameType.Parchis) {
            for (int i = 0; i < 4; i++) {
                GamePiece parchis_piece = new GamePiece();
                parchis_piece.setTokenColor(color);
                parchis_piece.setUser_id(user);
                gamePieces.add(parchis_piece);
                this.gamePieceRepository.save(parchis_piece);
            }
        }
        else
        { 
            GamePiece oca_piece = new GamePiece();
            oca_piece.setTokenColor(color);
            oca_piece.setUser_id(user);
            gamePieces.add(oca_piece);
            this.gamePieceRepository.save(oca_piece);


        }
        return gamePieces;
    }

    public boolean checkUserAlreadyinGame(User user)
    {
        List<Game> all_games = new ArrayList<>();
        this.gameRepository.findAll().forEach(all_games::add);
        for(Game game : all_games)
        {
            System.out.println("hello");
            if(game.getOther_players().contains(user))
                return true;
        }
        return false;
    }

    public boolean gameNameExists(Game game)
    {
        return this.gameRepository.existsByName(game.getName());
    }


}
