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
        game.rollDice();
        logger.info("Dice Rolled: " + game.dice);
        game.setTurn_state(TurnState.CHOOSEPLAY);
        parchisService.handleState(game);
}

}
