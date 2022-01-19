package org.springframework.samples.parchisoca.game.AI;

import java.util.List;

import org.springframework.samples.parchisoca.game.BoardFieldService;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.Option;
import org.springframework.samples.parchisoca.game.OptionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AIStrategy {
    Boolean checkStrategy(List<Option> options, Game game, BoardFieldService boardFieldService, OptionService optionService);

    public static final Logger logger = LoggerFactory.getLogger(AIStrategy.class);

    
    StrategyName getStrategyName();
}
