package org.springframework.samples.parchisoca.game.AI;

import java.util.List;

import org.springframework.samples.parchisoca.game.Option;

public interface AIStrategy {
    void checkStrategy(List<Option> options);
    
    StrategyName getStrategyName();
}
