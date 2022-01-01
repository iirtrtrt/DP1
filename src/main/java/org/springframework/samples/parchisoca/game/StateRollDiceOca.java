package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
public class StateRollDiceOca {


    private static final Logger logger = LogManager.getLogger(StateRollDiceOca.class);

    private static OcaService ocaService;
    @Autowired
    private OcaService ocaService_;

    private static TurnsService turnsService;
    @Autowired
    private TurnsService turnsService_;
  
    @PostConstruct     
    private void initStaticDao () {
       ocaService = this.ocaService_;
       turnsService = this.turnsService_;
    }

    public static void doAction(Game game){
        
            game.rollDice();
            System.out.println("Dice Rolled: " + game.dice);
            if(game.getTurns().size()<game.getMax_player()){
            
                Turns newturn = new Turns();
                newturn.setNumber(game.dice);
                
                newturn.setUser(game.getCurrent_player());
                
                game.setTurn_state(TurnState.DIRECTPASS);
                turnsService.saveTurn(newturn);
                try {
                game.addTurn(newturn);
                }catch(Exception e){
                    logger.error("ERROR: Game has not been created!");
                }
                
            
            }else{
            game.setTurn_state(TurnState.CHOOSEPLAY);
            }
        
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