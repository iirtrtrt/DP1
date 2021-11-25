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

@Disabled
public class BoardfieldServiceTest {

    @Autowired
    BoardFieldService boardFieldService;

    @Autowired
    GameService gameService;

    @Test
    void saveAndFindBoardField()
    {

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

}
