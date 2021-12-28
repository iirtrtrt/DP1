
package org.springframework.samples.parchisoca.game;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;


@Component
public class StateInit {


    private static final Logger logger = LoggerFactory.getLogger(StateInit.class);

    private static UserService userService;
    @Autowired
    private UserService userService_;


    @PostConstruct
    private void initStaticDao () {
       userService = this.userService_;
    }


    public static void doAction(Game game){
        logger.info("current player: " +  game.getCurrent_player().getUsername());

        if (game.getCurrent_player() == userService.getCurrentUser().get()) {
            userService.getCurrentUser().get().setMyTurn(true);
            logger.info("The current user has been found:");
        }
    }
}
