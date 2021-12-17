
package org.springframework.samples.parchisoca.game;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;


@Component
public class StateInit {

    private static UserService userService;
    @Autowired
    private UserService userService_;


    @PostConstruct     
    private void initStaticDao () {
        System.out.println("in Construct");
       userService = this.userService_;
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
    }
}