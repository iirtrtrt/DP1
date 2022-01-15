package org.springframework.samples.parchisoca.game.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;
import org.springframework.stereotype.Service;

@Service
public class StrategyFactory {

  private List<AIStrategy> strategies;

  @Autowired
  public StrategyFactory(Set<AIStrategy> strategySet) {
     createStrategy(strategySet);
  }

  public void findStrategy(List<Option> options, Game game, BoardFieldService boardFieldService, OptionService optionService) {

      Boolean found = false;
      for(AIStrategy strategy : strategies){
         found = strategy.checkStrategy(options, game, boardFieldService, optionService);
         if(found){
            break;
         }
      }

}


  private void createStrategy(Set<AIStrategy> strategySet) {
      strategies = new ArrayList<AIStrategy>();
      strategySet.forEach(
        strategy ->strategies.add(strategy));
  }
}


