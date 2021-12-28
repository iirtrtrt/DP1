package org.springframework.samples.parchisoca.game;

import org.springframework.samples.parchisoca.user.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import java.util.List;

@Component
public class StatePassMove {

    private static final Logger logger = LogManager.getLogger(StatePassMove.class);

    private static BoardFieldService boardFieldService;
    @Autowired
    private BoardFieldService boardFieldService_;


    private static ParchisService parchisService;
    @Autowired
    private ParchisService parchisService_;

    @PostConstruct
    private void initStaticDao () {
       boardFieldService = this.boardFieldService_;
       parchisService = this.parchisService_;
    }

    public static void doAction(Game game){
        
        int index_last_player = game.getCurrent_players().indexOf(game.getCurrent_player());
        logger.info("Index of current player:" + index_last_player);

        if (index_last_player == game.getCurrent_players().size() - 1) {
            List<Turns> turns = game.getTurns();
            Turns definitiveTurn = new Turns();
            Integer min = 100;
            for(Turns turn : turns){
                if(turn.getNumber()<min){
                    min=turn.getNumber();
                    definitiveTurn=turn;
                }
            }
            String firstUsername = definitiveTurn.getUsername();
            User newUser = new User();
            for(User user: game.getCurrent_players()){
                if(user.getUsername().equals(firstUsername)){
                newUser=user;
                break;
                }
            }
              game.setCurrent_player(newUser);
        }
        game.setTurn_state(TurnState.NEXT);
        parchisService.handleState(game);
    }

    

}
