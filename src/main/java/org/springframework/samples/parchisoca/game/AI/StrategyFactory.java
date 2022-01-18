package org.springframework.samples.parchisoca.game.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;
import org.springframework.stereotype.Service;

@Service
public class StrategyFactory {

  private List<AIStrategy> strategies = new ArrayList<AIStrategy>();


  private static final Logger logger = LogManager.getLogger(StrategyFactory.class);


  @Autowired
  public StrategyFactory(Set<AIStrategy> strategySet) {
     createStrategy(strategySet);
  }

  public void findStrategy(List<Option> options, Game game, BoardFieldService boardFieldService, OptionService optionService) {
      logger.info("Testing Strategies " + strategies.size());
      for(AIStrategy strategy : strategies){
         logger.info("Testing Strategy: " + strategy.getStrategyName());
      }


      Boolean found = false;
      for(AIStrategy strategy : strategies){
         found = strategy.checkStrategy(options, game, boardFieldService, optionService);
         if(found){
            logger.info("Strategy found: " + strategy.getStrategyName());
            break;
         }
      }
   }

  private void createStrategy(Set<AIStrategy> strategySet) {
     logger.info("Generating Strategies: " + strategySet.size());
      strategySet.forEach(
        strategy ->strategies.add(strategy));
  }
}


