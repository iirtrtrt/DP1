package org.springframework.samples.parchisoca.model.game.AI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.service.GameService;
import org.springframework.samples.parchisoca.model.game.Oca;
import org.springframework.samples.parchisoca.service.OcaService;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.samples.parchisoca.model.game.Parchis;
import org.springframework.samples.parchisoca.service.ParchisService;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.stereotype.Service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;






@Service
public class AIService {

       GameService gameService;
       OptionService optionService;
       BoardFieldService boardFieldService;

       @Autowired
       private StrategyFactory strategyFactory;

       private static final Logger logger = LogManager.getLogger(StrategyFactory.class);


       @Autowired
       AIService(GameService gameservice,  StrategyFactory strategyFactory, BoardFieldService boardFieldService, OptionService optionService){
              this.gameService = gameservice;
              this.strategyFactory = strategyFactory;
              this.boardFieldService = boardFieldService;
              this.optionService = optionService;
       }

	public void choosePlay(Game game, ParchisService parchisService) {
              Parchis parchis = (Parchis) game.getGameboard();
              List<Option> options = parchis.getOptions();
             // User ai = game.getCurrent_player();

             logger.info("AI choosing Move");

             if(options.size() == 1){
                    logger.info("AI: Only one option to choose from");
                    options.get(0).setChoosen(true);
                    optionService.saveOption(options.get(0));
             }
             else{
                     strategyFactory.findStrategy(options, game, boardFieldService, optionService);
                     boolean set = false;
                     for(Option option : options) {
                            if(option.getChoosen()) set = true;
                     }
                     if(!set){
                            options.get(0).setChoosen(true);
                            for(Option option : options){
                                   logger.error("no option found " + option.getText());
                            }
                     }


             }

              if(game.getTurn_state().equals(TurnState.DIRECTPASS)){
                     game.setTurn_state(TurnState.PASSMOVE);
              }else if(game.getTurn_state().equals(TurnState.CHOOSEEXTRA)){
                     game.setTurn_state(TurnState.EXTRA);
              }else{
                     game.setTurn_state(TurnState.MOVE);
              }

              gameService.saveGame(game);
              parchisService.handleState(game);


	}

       public void choosePlay(Game game, OcaService ocaservice) {
              Oca oca = (Oca) game.getGameboard();
              List<Option> options = oca.getOptions();
              User ai = game.getCurrent_player();
              options.get(0).setChoosen(true);

              //More Logic here

              if(game.getTurn_state().equals(TurnState.DIRECTPASS)){
                     game.setTurn_state(TurnState.PASSMOVE);

              }
              else{
                     game.setTurn_state(TurnState.MOVE);
              }
              gameService.saveGame(game);
              ocaservice.handleState(game);
	}



}
