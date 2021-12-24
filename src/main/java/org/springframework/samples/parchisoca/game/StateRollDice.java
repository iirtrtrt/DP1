package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.UserValidator;
import org.springframework.stereotype.Component;


@Component
public class StateRollDice {



    private static final Logger logger = LogManager.getLogger(StateRollDice.class);


    private static ParchisService parchisService;
    @Autowired
    private ParchisService parchisService_;

    @PostConstruct
    private void initStaticDao () {
       parchisService = this.parchisService_;
    }

    public static void doAction(Game game){
        if(game.getValuesPerPlayer().size()<game.getMax_player()){
            game.rollDice();
            System.out.println("Dice Rolled: " + game.dice);
            game.getValuesPerPlayer().put(game.getCurrent_player(), game.getDice());
            game.setTurn_state(TurnState.CHOOSEPLAY);
            parchisService.handleState(game);

        }
        else{
        game.rollDice();
        logger.info("Dice Rolled: " + game.dice);
        game.setTurn_state(TurnState.CHOOSEPLAY);
        parchisService.handleState(game);}
}

}
