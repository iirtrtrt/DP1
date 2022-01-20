package org.springframework.samples.parchisoca.model.game;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.stereotype.Component;

@Component
public class StateDirectPass {

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
        Parchis parchis = (Parchis) game.getGameboard();
        parchis.options = new ArrayList<>();
        Option op = new Option(0, Option.ORDER);
        optionService.saveOption(op);
        parchis.options.add(op);
    }





}
