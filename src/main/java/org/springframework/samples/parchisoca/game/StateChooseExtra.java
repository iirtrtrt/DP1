package org.springframework.samples.parchisoca.game;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;

public class StateChooseExtra {

    private static BoardFieldService boardFieldService;
    @Autowired
    private BoardFieldService boardFieldService_;


    private static ParchisService parchisService;
    @Autowired
    private ParchisService parchisService_;

    @PostConstruct
    private void initStaticDao () {
       boardFieldService = this.boardFieldService_;
       parchisService = this.parchisService_;
    }

    public static void doAction(Game game){    
        Parchis parchis = (Parchis) game.getGameboard();
        parchis.options = new ArrayList<>();
        StateChoosePlay.optionCreator(game.getCurrent_player().getGamePieces(), game);
        game.setTurn_state(TurnState.EXTRA);
        parchisService.handleState(game);
    }

}
