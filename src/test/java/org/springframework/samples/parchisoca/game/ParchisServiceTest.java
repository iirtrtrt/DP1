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


}
