package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.stereotype.Component;


@Component
public class StateRollDiceOca {



    private static OcaService ocaService;
    @Autowired
    private OcaService ocaService_;
  
    @PostConstruct     
    private void initStaticDao () {
       ocaService = this.ocaService_;
    }

    public static void doAction(Game game){
        game.rollDice();
        System.out.println("Dice Rolled: " + game.dice);
        game.setTurn_state(TurnState.CHOOSEPLAY);
        ocaService.handleState(game);

        // game.rollDice();
        // System.out.println("Dice Rolled: " + game.dice);
        // GamePiece movingPiece = game.getCurrent_player().getGamePieces().get(0);
        // //Integer nextPos = movingPiece.getField().getNext_field().getNumber() + game.getDice() -1;
        // //Implement the actual move here!
        // BoardField nextField = boardFieldService.find(game.getDice(), game.getGameboard());
        // movingPiece.setField(nextField);

        // game.setTurn_state(TurnState.NEXT);
        // handleState(game);
}
    
}