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
public class EStrategy_MoveFrontPlayer implements AIStrategy{

    private static final Map < Color, Integer> color_endzone = Map.of(
        Color.RED,  34,
        Color.BLUE, 17,
        Color.GREEN, 51,
        Color.YELLOW, 68
    );
    
    @Override
    public Boolean checkStrategy(List<Option> options,  Game game, BoardFieldService boardFieldService, OptionService optionService){
        logger.info("Testing Strategy: " + this.getStrategyName());
        Integer furthest_fieldnumber = 0;
        Option choosen_option = options.get(0);

        for(Option option : options ){
            Integer field_number = Integer.parseInt(option.getText().substring(Option.MOVE.length()));
            if(field_number > 100) continue;
            field_number =  field_number <= color_endzone.get(game.getCurrent_player().getTokenColor()) ? field_number + 68 : field_number;


            if(field_number > furthest_fieldnumber){
                choosen_option = option;
                furthest_fieldnumber = field_number;
            }
        }
        choosen_option.setChoosen(true);
        return true;
    }

    @Override
    public StrategyName getStrategyName(){
        return StrategyName.Front;
    }
    
}
