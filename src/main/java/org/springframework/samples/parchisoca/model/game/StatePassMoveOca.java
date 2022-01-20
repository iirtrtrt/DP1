package org.springframework.samples.parchisoca.model.game;

import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.service.OcaService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.model.user.User;

@Component
public class StatePassMoveOca {

    private static final Logger logger = LogManager.getLogger(StatePassMoveOca.class);

    private static BoardFieldService boardFieldService;
    @Autowired
    private BoardFieldService boardFieldService_;


    private static OcaService ocaService;
    @Autowired
    private OcaService ocaService_;

    @PostConstruct
    private void initStaticDao () {
       boardFieldService = this.boardFieldService_;
       ocaService = this.ocaService_;
    }

    public static void doAction(Game game){

        int index_last_player = game.getCurrent_players().indexOf(game.getCurrent_player());
        logger.info("Index of current player:" + index_last_player);

        if (index_last_player == game.getCurrent_players().size() - 1) {
            Map<User,Integer> map = new HashMap<>();
        List<Turns> listTurns = game.getTurns();
        for(Turns turn : listTurns){
            map.put(turn.getUser_id(), turn.getNumber());
        }
        Map<User,Integer> mapOrdered = map.entrySet().stream()
                                 .sorted((Map.Entry.<User,Integer>comparingByValue().reversed()))
                                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2)->e1, LinkedHashMap::new));

        List<User> turns = mapOrdered.keySet().stream().collect(Collectors.toList());

        User definitiveTurn = new User();

        definitiveTurn=turns.get(turns.size()-1);

        User newUser = definitiveTurn;

        game.setCurrent_player(newUser);
        }
        game.setTurn_state(TurnState.NEXT);
        ocaService.handleState(game);
    }

}
