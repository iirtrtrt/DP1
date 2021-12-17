package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;

import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;


@Component
public class StateNext {

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
          System.out.println("Index of current player:" + index_last_player);
          System.out.println("Size of List: " + game.getCurrent_players().size());

          if (index_last_player == game.getCurrent_players().size() - 1) {
              //next player is the first one in the list
              game.setCurrent_player(game.getCurrent_players().get(0));
              System.out.println("Current player after setting if: " + game.getCurrent_player().getUsername());

          } else {
              //next player is the next one in the list
              game.setCurrent_player(game.getCurrent_players().get(index_last_player + 1));
              System.out.println("Current player after setting else: " + game.getCurrent_player().getUsername());
          }
          game.setTurn_state(TurnState.INIT);
          System.out.println("Current player after setting " + game.getCurrent_player().getUsername());

          userService.getCurrentUser().get().setMyTurn(false);
          parchisService.handleState(game);
    }
    
}
