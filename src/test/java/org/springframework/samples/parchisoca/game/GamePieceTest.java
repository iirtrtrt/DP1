package org.springframework.samples.parchisoca.game;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.model.game.BoardField;
import org.springframework.samples.parchisoca.model.game.GamePiece;
import org.springframework.samples.parchisoca.model.user.User;


public class GamePieceTest {

    static int id = 1;

    GamePiece createTestGamePiece(){
        GamePiece piece = new GamePiece();
        piece.setGamePiece_id(id++);
        piece.setTokenColor(Color.RED);

        return piece;
    }




    @Test
    public void GamePieceInBoundsOfFieldParchis(){



        BoardField boardField = new BoardField(1, "#fef9e7", FieldType.HORIZONTAL, 2, 2, 2, 1);
        boardField.setNumber(4);


        GamePiece gamePiece = createTestGamePiece();
        gamePiece.setField(boardField);


        boardField.setListGamesPiecesPerBoardField(new ArrayList<>());
        boardField.getListGamesPiecesPerBoardField().add(gamePiece);

        User user = new User();
        user.getGamePieces().add(gamePiece);
        gamePiece.setUser_id(user);

        int size = 10;

        int x = gamePiece.getPositionXInPixels(size);
        int y = gamePiece.getPositionYInPixels(size);
        assertTrue("x value too far left", x > boardField.getPositionXluInPixels(size));
        assertTrue("y value too far north", y > boardField.getPositionYluInPixels(size));

        assertTrue("x value too far right", x < boardField.getPositionXluInPixels(size) + boardField.getPositionXrbInPixels(size));
        assertTrue("y value too far south", y < boardField.getPositionYluInPixels(size) + boardField.getPositionYrbInPixels(size));
    }

    @Test
    public void GamePieceInBoundsOfFieldOca(){
        BoardField boardField = new BoardField(1, "#fef9e7", FieldType.SQUARE, 2, 2, 2, 1);
        boardField.setNumber(4);


        GamePiece gamePiece = createTestGamePiece();
        gamePiece.setField(boardField);


        boardField.setListGamesPiecesPerBoardField(new ArrayList<>());
        boardField.getListGamesPiecesPerBoardField().add(gamePiece);


        int size = 10;

        int x = gamePiece.getPositionXInPixelsOca(size);
        int y = gamePiece.getPositionYInPixelsOca(size);

        assertTrue("x value too far left", x > boardField.getPositionXluInPixels(size));
        assertTrue("y value too far north", y > boardField.getPositionYluInPixels(size));
        assertTrue("x value too far right", x < boardField.getPositionXluInPixels(size) + boardField.getPositionXrbInPixels(size));
        assertTrue("y value too far south", y < boardField.getPositionYluInPixels(size) + boardField.getPositionYrbInPixels(size));
    }


    @Test
    public void TwoPiecesOnOneField(){
        BoardField boardField = new BoardField(1, "#fef9e7", FieldType.HORIZONTAL, 2, 2, 2, 1);

        GamePiece gamePiece = createTestGamePiece();
        GamePiece gamePiece2 = createTestGamePiece();
        gamePiece2.setTokenColor(Color.YELLOW);

        gamePiece.setField(boardField);
        gamePiece2.setField(boardField);
        boardField.setListGamesPiecesPerBoardField(new ArrayList<>());
        boardField.getListGamesPiecesPerBoardField().add(gamePiece);
        boardField.getListGamesPiecesPerBoardField().add(gamePiece2);

        int size = 10;

        int x = gamePiece.getPositionXInPixelsOca(size);
        int y = gamePiece.getPositionYInPixelsOca(size);

        int x2 = gamePiece2.getPositionXInPixelsOca(size);
        int y2 = gamePiece2.getPositionYInPixelsOca(size);

        assertTrue("two pieces on same spot not possible", !(x == x2 && y == y2));



    }



}
