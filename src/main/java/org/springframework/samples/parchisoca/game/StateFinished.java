package org.springframework.samples.parchisoca.game;

import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.stereotype.Component;

import java.awt.Color;
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
