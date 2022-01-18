package org.springframework.samples.parchisoca.game.AI;

import java.util.List;

import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;

public interface AIStrategy {
    Boolean checkStrategy(List<Option> options, Game game, BoardFieldService boardFieldService, OptionService optionService);
    
    StrategyName getStrategyName();
}
