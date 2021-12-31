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
        Option op = new Option();
        op.setNumber(1);
        op.setText("Pass turn");
        optionService.saveOption(op);
        oca.options.add(op);
    }

}
