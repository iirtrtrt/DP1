package org.springframework.samples.parchisoca.game;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
public class StateNextOca {

    private static final Logger logger = LogManager.getLogger(StateNextOca.class);

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
        
        Map<User,Integer> mapa = new HashMap<>();
        List<Turns> listTurns = game.getTurns();
        for(Turns turn : listTurns){
            mapa.put(turn.getUser_id(), turn.getNumber());
        }
        Map<User,Integer> mapaOrdenado = mapa.entrySet().stream()
                                 .sorted((Map.Entry.<User,Integer>comparingByValue().reversed()))
                                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2)->e1, LinkedHashMap::new));

        System.out.println("Final order: " + mapaOrdenado);
                    
        List<User> turns = mapaOrdenado.keySet().stream().collect(Collectors.toList());
        //get the player whos turn is next (simulate a loop)
          int index_last_player = turns.indexOf(game.getCurrent_player());
          logger.info("Index of current player:" + index_last_player);

          if (index_last_player == turns.size() - 1) {
              //next player is the first one in the list
              User newUser = turns.get(0);
              
              game.setCurrent_player(newUser);

          } else {
              //next player is the next one in the list
              User newUser = turns.get(index_last_player+1);
              
              game.setCurrent_player(newUser);
          }
          game.setTurn_state(TurnState.INIT);

          userService.getCurrentUser().get().setMyTurn(false);
          ocaService.handleState(game);
    }



    public static void doActionI(Game game){
        int index_last_player = game.getCurrent_players().indexOf(game.getCurrent_player());
        System.out.println("Index of current player" + game.getCurrent_player().getUsername() + ": " + index_last_player);
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
        
        ocaService.handleState(game);
    }
}