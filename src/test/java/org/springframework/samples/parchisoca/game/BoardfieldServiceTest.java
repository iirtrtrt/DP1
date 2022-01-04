package org.springframework.samples.parchisoca.game;

import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.EmailService;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

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

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        EmailService.class }))
public class BoardfieldServiceTest {

    @Autowired
    BoardFieldService boardFieldService;

    @Autowired
    GameService gameService;

    @Test
    void saveAndFindBoardField() {

        Game game = new Game();
        game.setName("test");
        this.gameService.saveGame(game);

        GameBoard gameBoard = new GameBoard();
        gameBoard.setWidth(800);
        gameBoard.setHeight(800);
        this.gameService.saveGameBoard(gameBoard, game);

        BoardField boardField = new BoardField();
        boardField.setBoard(gameBoard);
        boardField.setNumber(1);
        this.boardFieldService.saveBoardField(boardField);

        Assertions.assertNotNull(this.boardFieldService.find(1, gameBoard));
    }

    @Test
    void checkNextField() {
        Game game = new Game();
        game.setName("test");
        this.gameService.saveGame(game);

        GameBoard gameBoard = new GameBoard();
        gameBoard.setWidth(800);
        gameBoard.setHeight(800);
        this.gameService.saveGameBoard(gameBoard, game);

        BoardField firstField = new BoardField();
        BoardField secondField = new BoardField();

        firstField.setBoard(gameBoard);
        firstField.setNumber(1);
        firstField.setNext_field(secondField);
        this.boardFieldService.saveBoardField(firstField);

        secondField.setBoard(gameBoard);
        secondField.setNumber(2);
        this.boardFieldService.saveBoardField(secondField);

        Assertions.assertTrue(this.boardFieldService.getNext_fieldByNumberAndBoard(1, gameBoard) == secondField);
    }
}
