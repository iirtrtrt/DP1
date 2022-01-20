package org.springframework.samples.parchisoca.model.game;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.service.OptionService;
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
        optionCreator2(game.getCurrent_player().getGamePieces().get(0), oca);
    }

    private static void optionCreator2(GamePiece piece, Oca oca) {

        Option op = new Option(0, Option.ORDER);
        optionService.saveOption(op);
        oca.options.add(op);
    }

}
