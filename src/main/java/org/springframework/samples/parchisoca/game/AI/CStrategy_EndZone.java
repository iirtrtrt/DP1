package org.springframework.samples.parchisoca.game.AI;

import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import java.awt.*;

@Service
public class CStrategy_EndZone implements AIStrategy{

    private static final Map < Color, Integer> color_endzone = Map.of(
        Color.RED,  34,
        Color.BLUE, 17,
        Color.GREEN, 51,
        Color.YELLOW, 68
    );

    
    @Override
    public Boolean checkStrategy(List<Option> options,  Game game, BoardFieldService boardFieldService, OptionService optionService){
        logger.info("Testing Strategy: " + this.getStrategyName());

        for(Option option : options){
            Integer field_number = Integer.parseInt(option.getText().substring(Option.MOVE.length()));
            //check if player is standing before endzone
            Integer field_before_endzone = color_endzone.get(game.getCurrent_player().getTokenColor());
            if(field_number <= field_before_endzone && field_number > field_before_endzone - game.getDice()){
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
