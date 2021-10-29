package org.springframework.samples.petclinic.game;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.enums.GameStatus;
import org.springframework.samples.petclinic.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
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

    public List<Game> findGameByStatus(GameStatus status) throws DataAccessException {
        return gameRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Optional<Game> findGamebyID(Integer id) throws DataAccessException {
        return gameRepository.findById(id);
    }

}
