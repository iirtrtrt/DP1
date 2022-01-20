
package org.springframework.samples.parchisoca.model.game;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.service.ParchisService;
import org.springframework.samples.parchisoca.model.user.UserRole;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.stereotype.Component;


@Component
public class StateInit {


    private static final Logger logger = LoggerFactory.getLogger(StateInit.class);

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


        if (game.getCurrent_player() == userService.getCurrentUser().get()) {
            userService.getCurrentUser().get().setMyTurn(true);
            logger.info("The current user has been found:");
        }
        else if(game.getCurrent_player().getRole() == UserRole.AI && game.getCreator() == userService.getCurrentUser().get() ){
            //AI is next player
            logger.info("AI is the next player");
            game.getCurrent_player().setMyTurn(true);
            game.setTurn_state(TurnState.ROLLDICE);
            parchisService.handleState(game);
        }
    }
}
