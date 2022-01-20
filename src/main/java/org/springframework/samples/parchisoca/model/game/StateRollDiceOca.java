package org.springframework.samples.parchisoca.model.game;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.service.OcaService;
import org.springframework.samples.parchisoca.service.TurnsService;
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
            if(game.getTurns().size()<game.getMax_player()){

                Turns newturn = new Turns();
                newturn.setNumber(game.dice);

                newturn.setUser_id(game.getCurrent_player());

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

}

}
