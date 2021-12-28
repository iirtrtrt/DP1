package org.springframework.samples.parchisoca.game;


import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.EmailService;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import java.awt.*;
import java.util.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class GameServiceTests {

    @Autowired
    GameService gameService;

   @Autowired
   UserService userService;



    @Test
    public void saveGameAndFind() throws InterruptedException {
        Game game = new Game();
        game.setName("test");

        try {
            gameService.saveGame(game);
        }
        catch (Exception e )
        {
        }

        Optional<Game> optionalGame = gameService.findGameByName("test");
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
        }

        List<Game> games = gameService.findAllGames();
        Assertions.assertEquals(games.size(), 2);
        Assertions.assertEquals(games.get(0).getName(), ("test1"));
        Assertions.assertEquals(games.get(1).getName(), "test2");
    }


    @Test
    public void createPachisGamePiecesAndFind() throws InterruptedException {

        Game game = new Game();
        game.setType(GameType.Parchis);
        game.setName("new_game");
        this.gameService.saveGame(game);
        Optional<User> optionalUser = this.userService.findUser("flogam1");

        if(optionalUser.isEmpty())
            Assertions.fail("User does not exist ");

        User found_user = optionalUser.get();

        this.gameService.createGamePieces(found_user, game, Color.YELLOW);
        List<GamePiece> gamePieces  = found_user.getGamePieces();
        Assertions.assertEquals(gamePieces.size(), 4);
    }

    @Test
    public void createOcaGamePiecesAndFind() throws InterruptedException {

        Game game = new Game();
        game.setType(GameType.Oca);
        game.setName("new_game");
        this.gameService.saveGame(game);
        Optional<User> optionalUser = this.userService.findUser("flogam1");

        if(optionalUser.isEmpty())
            Assertions.fail("User does not exist ");

        User found_user = optionalUser.get();

        this.gameService.createGamePieces(found_user, game, Color.YELLOW);
        List<GamePiece> gamePieces  = found_user.getGamePieces();
        Assertions.assertEquals(gamePieces.size(), 1);
    }

    @Test
    public void createGameAndCheckIfAlreadyInGame() throws InterruptedException {

        Game game = new Game();
        game.setType(GameType.Oca);
        game.setName("new_game");
        Optional<User> optionalUser1 = this.userService.findUser("flogam1");
        Optional<User> optionalUser2 = this.userService.findUser("admin1");

        if(optionalUser1.isEmpty() || optionalUser2.isEmpty())
            Assertions.fail("User does not exist ");

        User creator_user = optionalUser1.get();
        User joining_user = optionalUser1.get();
        game.setCreator(creator_user);

        game.setOther_players( Arrays.asList(joining_user));
        this.gameService.saveGame(game);

       Assertions.assertTrue(this.gameService.checkUserAlreadyinGame(joining_user));
    }

    @Test
    public void createGamesAndCheckIfNameAlreadyExists() throws InterruptedException {

        Game game1 = new Game();
        game1.setType(GameType.Oca);
        game1.setName("new_game");
        gameService.saveGame(game1);

        Game game2 = new Game();
        game2.setType(GameType.Oca);
        game2.setName("new_game");
        gameService.saveGame(game2);


        Assertions.assertTrue(this.gameService.gameNameExists(game2));
    }

}
