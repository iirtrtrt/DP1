package org.springframework.samples.parchisoca.game;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ParchisServiceTest {

    @Autowired
    ParchisService parchisService;
    


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

    




}
