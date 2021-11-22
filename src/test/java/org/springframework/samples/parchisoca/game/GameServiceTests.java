package org.springframework.samples.parchisoca.game;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.AssertFalse;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class GameServiceTests {

    @Autowired
    GameService gameService;



    @Test
    public void saveGameAndFind() throws InterruptedException {
        Game game = new Game();
        game.setName("test");

        try {
            gameService.saveGame(game);
        }
        catch (Exception e )
        {
            System.out.println("exception: " + e.getMessage());
        }

        List<Game> all_games = gameService.findAllGames();
        Optional<Game> optionalGame = gameService.findGamebyID(1);
        assertTrue(optionalGame.isPresent());
        assertTrue(optionalGame.get().getName().equals("test"));
    }

    @Test
    public void saveGameAndFindByStatus() throws InterruptedException {
        Game game = new Game();
        game.setName("test");


        try {
            gameService.saveGame(game);
        }
        catch (Exception e )
        {
            System.out.println("exception: " + e.getMessage());
        }

        List<Game> games = gameService.findGameByStatus(GameStatus.CREATED);
        assertTrue(games.size() == 1);
        assertTrue(games.get(0).getName().equals("test"));
    }

    @Test
    public void saveMultipleGamesAndSearchAll() throws InterruptedException {


        List<Game> all_games = new ArrayList<>();
        Game game_1 = new Game();
        game_1.setName("test1");

        Game game_2 = new Game();
        game_2.setName("test2");

        all_games.add(game_1);
        all_games.add(game_2);

        try {
            gameService.saveGames(all_games);
        }
        catch (Exception e )
        {
            System.out.println("exception: " + e.getMessage());
        }

        List<Game> games = gameService.findAllGames();
        assertTrue(games.size() == 2);
        assertTrue(games.get(0).getName().equals("test1"));
        assertTrue(games.get(1).getName().equals("test2"));
    }
}
