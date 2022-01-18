package org.springframework.samples.parchisoca.game.AI;

import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;

import java.util.List;
import java.util.Map;

import java.awt.*;


public class CStrategy_EndZone implements AIStrategy{

    private static final Map < Color, Integer> color_end = Map.of(
        Color.RED,  134,
        Color.BLUE, 117,
        Color.GREEN, 151,
        Color.YELLOW, 168
    );

    
    @Override
    public Boolean checkStrategy(List<Option> options,  Game game, BoardFieldService boardFieldService, OptionService optionService){
        //implement check here

        for(Option option : options){
            Integer field_number = Integer.parseInt(option.getText().substring(Option.MOVE.length() - 1));
            if(field_number >= color_end.get(game.getCurrent_player().getTokenColor())){
                option.setChoosen(true);
                optionService.saveOption(option);
                return true;
            }
        }
        return false;
    }

    @Override
    public StrategyName getStrategyName(){
        return StrategyName.EndZone;
    }
    
}
