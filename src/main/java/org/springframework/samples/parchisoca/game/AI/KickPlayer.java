package org.springframework.samples.parchisoca.game.AI;

import org.springframework.samples.parchisoca.game.Option;
import java.util.List;


public class KickPlayer implements AIStrategy{
    
    @Override
    public void checkStrategy(List<Option> options){
        //implement check here
    }

    @Override
    public StrategyName getStrategyName(){
        return StrategyName.StrategyA;
    }
    
}
