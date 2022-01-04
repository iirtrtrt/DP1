package org.springframework.samples.parchisoca.game.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {

  private List<AIStrategy> strategies;
  
  @Autowired
  public StrategyFactory(Set<AIStrategy> strategySet) {
     createStrategy(strategySet);
  }
  
  public void findStrategy(List<Option> options) {
     //do sth
  }  
  
  
  private void createStrategy(Set<AIStrategy> strategySet) {
      strategies = new ArrayList<AIStrategy>();
      strategySet.forEach( 
        strategy ->strategies.add(strategy));
  }
}


    