package org.springframework.samples.parchisoca.game;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.GameStatus;
import java.time.LocalDateTime;

@Component
public class StateFinished {

    

    
    public static void doAction(Game game){
        game.setWinner(game.getCurrent_player());
        game.getCurrent_player().getWon_games().add(game);
        game.setEndTime(LocalDateTime.now());
        game.setHas_started(false);
        game.setStatus(GameStatus.FINISHED);
        game.setActionMessage(10);
    }

    
}
