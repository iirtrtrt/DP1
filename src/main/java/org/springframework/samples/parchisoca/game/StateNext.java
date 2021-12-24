package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;

import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;


@Component
public class StateNext {
    private static final Logger logger = LogManager.getLogger(StateNext.class);

    private static UserService userService;
    @Autowired
    private UserService userService_;


    private static ParchisService parchisService;
    @Autowired
    private ParchisService parchisService_;

    @PostConstruct
    private void initStaticDao () {
       userService = this.userService_;
       parchisService = this.parchisService_;
    }



    public static void doAction(Game game){
        //get the player whos turn is next (simulate a loop)
          //get the player whos turn is next (simulate a loop)
          int index_last_player = game.getCurrent_players().indexOf(game.getCurrent_player());
          logger.info("Index of current player:" + index_last_player);

          if (index_last_player == game.getCurrent_players().size() - 1) {
              //next player is the first one in the list
              game.setCurrent_player(game.getCurrent_players().get(0));

          } else {
              //next player is the next one in the list
              game.setCurrent_player(game.getCurrent_players().get(index_last_player + 1));
          }
          game.setTurn_state(TurnState.INIT);

          userService.getCurrentUser().get().setMyTurn(false);
          parchisService.handleState(game);
    }

}
