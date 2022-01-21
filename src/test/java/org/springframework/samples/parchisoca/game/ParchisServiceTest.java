package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.model.game.BoardField;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.service.GameService;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.samples.parchisoca.service.ParchisService;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.samples.parchisoca.enums.TurnState;

import java.awt.*;
import java.util.*;

import static org.junit.Assert.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class ParchisServiceTest {
    @Autowired
    GameService gameService;

    @Autowired
    OptionService optionService;

    @Autowired
    ParchisService parchisService;

    @Autowired
    UserService userService;

    @Autowired
    BoardFieldService boardFieldService;



    //Check the Gameboard Init method
    @Test
    public void checkIfBoardIsCreated(){
        Game game = new Game();
        game.setName("test");
        parchisService.initGameBoard(game);
        assertTrue(game.getGameboard() != null);
    }


    @Test
    public void checkIfBoardFieldsAreCreated(){
        Game game = new Game();
        game.setName("test");
        parchisService.initGameBoard(game);
        assertTrue(game.getGameboard().getFields().size() != 0);
    }

    @Test
    public void checkIfBoardFieldNumbersAreSet(){
        Game game = new Game();
        game.setName("test");
        parchisService.initGameBoard(game);
        for (BoardField field : game.getGameboard().getFields()){
            assertNotNull(field.getNumber());
        }
    }


    @Test
    @Disabled
    public void movePieceFromHome() throws InterruptedException{
        Game game = new Game();
        game.setType(GameType.Parchis);
        game.setName("new_game");
        game.setMax_player(1);
        this.gameService.saveGame(game);
        
        
        Optional<User> optionalUser = this.userService.findUser("flogam1");

        User found_user = optionalUser.get();
        gameService.createGamePieces(found_user, game, Color.YELLOW);
        parchisService.initGameBoard(game);
        
        game.setCurrent_player(found_user);
        found_user.setMyTurn(true);
        
        game.setDice(5);
        Option op = new Option(1, Option.MOVE);
        optionService.saveOption(op);
        op.setChoosen(true);
        game.getGameboard().setOptions(new ArrayList<Option>());
        game.getGameboard().options.add(op);
        this.gameService.saveGame(game);
        BoardField field = boardFieldService.find(1, game.getGameboard());
        
        game.setTurn_state(TurnState.MOVE);
        parchisService.handleState(game);
        Assertions.assertEquals(field.getNumber(), game.getCurrent_player().getGamePieces().get(0).getField().getNumber());
    }

}
