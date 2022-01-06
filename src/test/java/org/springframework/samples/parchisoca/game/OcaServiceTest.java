package org.springframework.samples.parchisoca.game;


import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.enums.ActionType;
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

public class OcaServiceTest {

    private static final int OCA_NUMBER_OF_FIELDS = 64;

    @Autowired
    OcaService ocaService;
    @Autowired
    GameService gameService;
    @Autowired
    UserService userService;
    @Autowired
    BoardFieldService boardFieldService;

    @Test
    void initGameBoardAndCheckNumberOfFields()
    {
        Game game = new Game();
        game.setName("test");
        this.gameService.saveGame(game);
        this.ocaService.initGameBoard(game);

        GameBoard gameBoard = game.getGameboard();
        assertTrue(gameBoard.getFields().size() == OCA_NUMBER_OF_FIELDS);

    }

    @Test
    void checkRollDice()
    {
        Game game = new Game();
        game.setName("test");
        this.gameService.saveGame(game);
        this.ocaService.initGameBoard(game);

        game.rollDice();
        Assertions.assertTrue(game.getDice()>=1 && game.getDice() <=6);

    }

    @Test
    void checkPieceMoving()
    {
        Game game = new Game();
        game.setName("test");
        Optional<User> optionalUser = this.userService.findUser("user");
        User found_user = optionalUser.get();
        game.setCurrent_player(found_user);
        game.setType(GameType.Oca);
        this.gameService.createGamePieces(found_user, game, Color.BLUE);
        game.getCurrent_player().getGamePieces().get(0).setField(boardFieldService.find(0, game.getGameboard()));
        this.gameService.saveGame(game);
        this.ocaService.initGameBoard(game);

        game.rollDice();
        
        GamePiece piece = game.getCurrent_player().getGamePieces().get(0);
        piece.setField(boardFieldService.find(piece.getField().getNumber() + game.getDice(), game.getGameboard()));
        Assertions.assertTrue(piece.getField().getNumber()==game.getDice());

    }

    @Test
    void checkGooseToGoose()
    {
        Game game = new Game();
        game.setName("test");
        Optional<User> optionalUser = this.userService.findUser("user");
        User found_user = optionalUser.get();
        game.setCurrent_player(found_user);
        game.setType(GameType.Oca);
        this.gameService.createGamePieces(found_user, game, Color.BLUE);
        game.getCurrent_player().getGamePieces().get(0).setField(boardFieldService.find(0, game.getGameboard()));
        this.gameService.saveGame(game);
        this.ocaService.initGameBoard(game);

        
        GamePiece piece = game.getCurrent_player().getGamePieces().get(0);
        piece.setField(boardFieldService.find(1, game.getGameboard()));
        Assertions.assertTrue(piece.getField().getAction().equals(ActionType.GOOSE) && piece.getField().getNext_field().getAction().equals(ActionType.GOOSE));

    }


}
