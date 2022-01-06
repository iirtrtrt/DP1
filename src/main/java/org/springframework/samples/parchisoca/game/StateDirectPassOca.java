package org.springframework.samples.parchisoca.game;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateDirectPassOca {

    private static OptionService optionService;
    @Autowired
    private OptionService optionService_;

    private static BoardFieldService boardFieldService;
    @Autowired
    private BoardFieldService boardFieldService_;




    @PostConstruct
    private void initStaticDao () {
        optionService = this.optionService_;
        boardFieldService = this.boardFieldService_;
    }

    public static void doAction(Game game){
        Oca oca = (Oca) game.getGameboard();
        oca.options = new ArrayList<>();
        //BoardField startField = boardFieldService.find(1, game.getGameboard());
        optionCreator2(game.getCurrent_player().getGamePieces().get(0), oca);
    }

    private static void optionCreator2(GamePiece piece, Oca oca) {

        Option op = new Option();
        op.setNumber(0);
        op.setText("Let's decide the order!! ");
        optionService.saveOption(op);
        oca.options.add(op);


    }

}
