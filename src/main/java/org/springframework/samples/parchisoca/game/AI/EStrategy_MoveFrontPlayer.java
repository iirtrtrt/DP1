package org.springframework.samples.parchisoca.game.AI;

import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;

import java.util.List;


public class EStrategy_MoveFrontPlayer implements AIStrategy{
    
    @Override
    public Boolean checkStrategy(List<Option> options,  Game game, BoardFieldService boardFieldService, OptionService optionService){
        
        Integer furthest_fieldnumber = Integer.parseInt(options.get(0).getText().substring(Option.MOVE.length() - 1));
        Option choosen_option = options.get(0);
        for(Option option : options ){
            Integer field_number = Integer.parseInt(option.getText().substring(Option.MOVE.length() - 1));
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
