package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.stereotype.Component;


@Component
public class StateRollDice {



    private static ParchisService parchisService;
    @Autowired
    private ParchisService parchisService_;
  
    @PostConstruct     
    private void initStaticDao () {
       parchisService = this.parchisService_;
    }

    public static void doAction(Game game){
        game.rollDice();
        System.out.println("Dice Rolled: " + game.dice);
        game.setTurn_state(TurnState.CHOOSEPLAY);
        parchisService.handleState(game);
}
    
}
