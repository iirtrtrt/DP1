
package org.springframework.samples.parchisoca.game;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.UserRole;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;


@Component
public class StateInit {

    private static UserService userService;
    @Autowired
    private UserService userService_;

    private static ParchisService parchisService;
    @Autowired
    private ParchisService parchisService_;


    @PostConstruct     
    private void initStaticDao () {
        System.out.println("in Construct");
       userService = this.userService_;
       parchisService = this.parchisService_;

    }


    public static void doAction(Game game){
        System.out.println("Current Player in Init: " + game.getCurrent_player().getUsername());
        if(userService == null){
            System.out.println("Service is Null");
        }
        if (game.getCurrent_player() == userService.getCurrentUser().get()) {
            userService.getCurrentUser().get().setMyTurn(true);
            System.out.println("The current user has been found:");
        }
        else if(game.getCurrent_player().getRole() == UserRole.AI && game.getCreator() == userService.getCurrentUser().get() ){
            //AI is next player 
            game.getCurrent_player().setMyTurn(true);
            game.setTurn_state(TurnState.ROLLDICE);
            parchisService.handleState(game);
        }
    }
}