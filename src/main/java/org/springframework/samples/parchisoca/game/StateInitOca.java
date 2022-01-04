
package org.springframework.samples.parchisoca.game;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.UserRole;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;


@Component
public class StateInitOca {

    private static UserService userService;
    @Autowired
    private UserService userService_;

    private static OcaService ocaService;
    @Autowired
    private OcaService ocaService_;


    @PostConstruct     
    private void initStaticDao () {
        System.out.println("in Construct");
       userService = this.userService_;
       ocaService = this.ocaService_;
    }


    public static void doAction(Game game){

        game.setActionMessage(0);
        if(game.getCurrent_player().getStunTurns() == null) game.getCurrent_player().setStunTurns(0);
        Integer stunTurns = game.getCurrent_player().getStunTurns();
        if(stunTurns==0){
            System.out.println("Current Player in Init: " + game.getCurrent_player().getUsername() + game.getCurrent_player().getRole() + game.getCreator());
            if (game.getCurrent_player() == userService.getCurrentUser().get()) {
                userService.getCurrentUser().get().setMyTurn(true);
            } 
            else if(game.getCurrent_player().getRole() == UserRole.AI && game.getCreator() == userService.getCurrentUser().get() ){
                //AI is next player 
                System.out.println("Ai is next player");
                game.getCurrent_player().setMyTurn(true);
                game.setTurn_state(TurnState.ROLLDICE);
            }
               
        }else{
            game.getCurrent_player().setStunTurns(stunTurns-1);
            game.setTurn_state(TurnState.NEXT);
            ocaService.handleState(game); 
        }

        
        
        
    }
}