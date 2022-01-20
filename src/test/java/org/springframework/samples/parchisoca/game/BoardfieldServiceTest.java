package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.model.game.BoardField;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.GameBoard;
import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.service.GameService;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

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
