package org.springframework.samples.parchisoca.game;

import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.samples.parchisoca.enums.TurnState;

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

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class ParchisServiceTest {
    @Autowired
    ParchisService gameService;

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
        parchisService.initGameBoard(game);
        assertTrue(game.getGameboard() != null);
    }


    @Test
    public void checkIfBoardFieldsAreCreated(){
        Game game = new Game();
        parchisService.initGameBoard(game);
        assertTrue(game.getGameboard().getFields().size() != 0);
    }

    @Test
    public void checkIfBoardFieldNumbersAreSet(){
        Game game = new Game();
        parchisService.initGameBoard(game);
        for (BoardField field : game.getGameboard().getFields()){
            assertNotNull(field.getNumber());
        }
    }
    @Test
    public void movePieceFromHome() throws InterruptedException{
        Game game = new Game();
        game.setType(GameType.Parchis);
        game.setName("new_game");
        this.gameService.saveParchis(game);
        Optional<User> optionalUser = this.userService.findUser("vazquez");


        
        BoardField field = boardFieldService.find(56, game.getGameboard());
        
        Assertions.assertEquals(game.getGameboard().getFields().get(33).getNumber(), game.getCurrent_players().get(0).getGamePieces().get(0).getField());
    }
    




}
