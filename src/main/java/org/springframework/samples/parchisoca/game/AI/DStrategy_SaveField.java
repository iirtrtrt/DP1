package org.springframework.samples.parchisoca.game.AI;

import org.springframework.samples.parchisoca.game.BoardField;
import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DStrategy_SaveField implements AIStrategy{
    
    @Override
    public Boolean checkStrategy(List<Option> options, Game game, BoardFieldService boardFieldService, OptionService optionService){
        logger.info("Testing Strategy: " + this.getStrategyName());

        for(Option option : options){
            Integer field_number = Integer.parseInt(option.getText().substring(Option.MOVE.length()));
            if(field_number > 100) continue;

            field_number =  field_number + game.getDice() <= 68 ?field_number + game.getDice() : field_number + game.getDice() - 68;

            BoardField field = boardFieldService.find(field_number, game.getGameboard());
            if(field != null && field.isParchis_special()){
                option.setChoosen(true);
                optionService.saveOption(option);
                return true;
                
            }
            
        }
        return false;
    }

    @Override
    public StrategyName getStrategyName(){
        return StrategyName.SaveField;
    }
    
}
