
package org.springframework.samples.parchisoca.model.game;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.service.OcaService;
import org.springframework.samples.parchisoca.model.user.UserRole;
import org.springframework.samples.parchisoca.service.UserService;
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
       userService = this.userService_;
       ocaService = this.ocaService_;
    }


    public static void doAction(Game game){

        game.setActionMessage(0);
        if(game.getCurrent_player().getStunTurns() == null) game.getCurrent_player().setStunTurns(0);
        Integer stunTurns = game.getCurrent_player().getStunTurns();
        if(stunTurns==0){
            if (game.getCurrent_player() == userService.getCurrentUser().get()) {
                userService.getCurrentUser().get().setMyTurn(true);
            }
            else if(game.getCurrent_player().getRole() == UserRole.AI && game.getCreator() == userService.getCurrentUser().get() ){
                //AI is next player
                game.getCurrent_player().setMyTurn(true);
                game.setTurn_state(TurnState.ROLLDICE);
                ocaService.handleState(game);
            }

        }else{
            game.getCurrent_player().setStunTurns(stunTurns-1);
            game.setTurn_state(TurnState.NEXT);
            ocaService.handleState(game);
        }




    }
}
