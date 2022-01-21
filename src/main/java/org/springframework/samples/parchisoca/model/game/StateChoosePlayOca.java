package org.springframework.samples.parchisoca.model.game;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.stereotype.Component;

@Component
public class StateChoosePlayOca {

    private static OptionService optionService;
    @Autowired
    private OptionService optionService_;



    @PostConstruct
    private void initStaticDao () {
        optionService = this.optionService_;
    }

    public static void doAction(Game game){
        Oca oca = (Oca) game.getGameboard();
        oca.options = new ArrayList<>();
        optionCreator(game.getCurrent_player().getGamePieces().get(0), oca);
    }

    private static void optionCreator(GamePiece piece, Oca oca) {

        Option op = new Option(piece.getField().getNumber(), Option.MOVE_OCA);
        optionService.saveOption(op);
        oca.options.add(op);


    }
}
