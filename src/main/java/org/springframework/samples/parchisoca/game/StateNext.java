package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        Parchis parchis = (Parchis) game.getGameboard();
        parchis.setRepetitions(0);
        Map<User,Integer> mapa = new HashMap<>();
        List<Turns> listTurns = game.getTurns();
        for(Turns turn : listTurns){
            mapa.put(turn.getUser(), turn.getNumber());
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
          parchisService.handleState(game);
    }

    

    public static void doActionI(Game game){
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
