package org.springframework.samples.parchisoca.game.AI;

import org.springframework.samples.parchisoca.game.BoardField;
import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BStrategy_KickPlayer implements AIStrategy{
    
    @Override
    public Boolean checkStrategy(List<Option> options,  Game game, BoardFieldService boardFieldService, OptionService optionService){

        for(Option option : options){
            Integer field_number = Integer.parseInt(option.getText().substring(Option.MOVE.length()));
            BoardField field = boardFieldService.find(field_number + game.getDice(), game.getGameboard());
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
