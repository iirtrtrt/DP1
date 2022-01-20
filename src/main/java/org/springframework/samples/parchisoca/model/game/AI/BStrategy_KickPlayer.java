package org.springframework.samples.parchisoca.model.game.AI;

import org.springframework.samples.parchisoca.model.game.BoardField;
import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BStrategy_KickPlayer implements AIStrategy{

    @Override
    public Boolean checkStrategy(List<Option> options,  Game game, BoardFieldService boardFieldService, OptionService optionService){
        logger.info("Testing Strategy: " + this.getStrategyName());

        for(Option option : options){
            Integer field_number = Integer.parseInt(option.getText().substring(Option.MOVE.length()));
            if(field_number > 100) continue;

            field_number =  field_number + game.getDice() <= 68 ? field_number + game.getDice() : field_number + game.getDice() - 68;
            logger.info("Field number to go = " + field_number);
            logger.info("Field number standing on " + option.getText().substring(Option.MOVE.length()) + " dice: " + game.getDice());
            BoardField field = boardFieldService.find(field_number, game.getGameboard());


            if(field.getListGamesPiecesPerBoardField().size() > 0){
                if(!field.isParchis_special()){

                    if(field.getListGamesPiecesPerBoardField().get(0).getUser_id() != game.getCurrent_player()){
                        option.setChoosen(true);
                        optionService.saveOption(option);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public StrategyName getStrategyName(){
        return StrategyName.KickPlayer;
    }

}
