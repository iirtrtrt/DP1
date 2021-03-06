package org.springframework.samples.parchisoca.model.game;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.stereotype.Component;

@Component
public class StateChooseExtra {

    private static OptionService optionService;

    @Autowired
    private OptionService optionService_;

    @PostConstruct
    private void initStaticDao () {
       optionService = this.optionService_;
    }

    public static void doAction(Game game){
        Parchis parchis = (Parchis) game.getGameboard();
        parchis.options = new ArrayList<>();
        StateChoosePlay.optionCreator(game.getCurrent_player().getGamePieces(), game);
        if(parchis.getOptions().size()==0){
            Option op = new Option(1, Option.PASS_EXTRA);
            optionService.saveOption(op);
            parchis.options.add(op);
        }
    }

}
