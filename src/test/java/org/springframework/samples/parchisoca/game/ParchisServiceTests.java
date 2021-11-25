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
import org.springframework.samples.parchisoca.enums.TurnState;
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


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class ParchisServiceTests {

    @Autowired
    ParchisService parchisService;

    @Autowired
    BoardFieldService boardFieldService;

    @Test
    public void movePieceFromHome() throws InterruptedException{
        Game game = new Game();
        parchisService.initGameBoard(game);
        User user1 = new User();
        user1.setTokenColor(Color.YELLOW);
        game.setMax_player(2);
        game.setCreator(user1);
        game.setCurrent_player(user1);
        game.setDice(5);
        game.setTurn_state(TurnState.CHOOSEPLAY);
        parchisService.handleState(game);


        
        BoardField field = boardFieldService.find(56, game.getGameboard());
        game.getCurrent_player().getGamePieces().get(0).setField(field);
        
        Assertions.assertEquals(game.getGameboard().getFields().get(33).getNumber(), game.getCurrent_players().get(0).getGamePieces().get(0).getField());
    }
}