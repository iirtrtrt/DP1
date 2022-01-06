package org.springframework.samples.parchisoca.game;

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
public class StateMoveOca {

    private static Boolean rep = false;

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

        
        // BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
        GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
        //BoardField dependant = boardFieldService.find(1, game.getGameboard());
        // for (Option opt: ocaBoard.getOptions()) {
        //     if (opt.getChoosen()) {
        //         System.out.println("The Choice is: " + opt.getText());
        //         fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
        //     }
        // }
        // selec.setField(dependant); 
        
        Integer nextPos =  calcPosition2(selec, game.getDice(), game);
        movePiece2(nextPos, selec, game);
        if(selec.getField().getNumber() == 63){
            game.setTurn_state(TurnState.FINISHED);
            ocaService.handleState(game);
        }
        if(rep == true){
            rep = false;
            game.setTurn_state(TurnState.ROLLDICE);
            ocaService.handleState(game);
        }else{
            game.setTurn_state(TurnState.NEXT);
            ocaService.handleState(game); 
        }  
        
        
    }

    private static Integer calcPosition2 (GamePiece piece, Integer moves, Game game){
        //Oca ocaBoard =(Oca) game.getGameboard();
        Integer x = piece.getField().getNumber();
        Integer nextPos =  (x+moves);
        // if (nextField.getAction() != null){
        //     if(nextField.getAction().equals(ActionType.DEATH)){ nextPos = 0; }
        //     else if(nextField.getAction().equals(ActionType.INN)){ game.getCurrent_player().setStunTurns(1); game.setActionMessage(0);}
        //     else if(nextField.getAction().equals(ActionType.WELL)){ game.getCurrent_player().setStunTurns(2);game.setActionMessage(0);}
        //     else if(nextField.getAction().equals(ActionType.MAZE)){ game.getCurrent_player().setStunTurns(3);game.setActionMessage(0);}
        //     else if(nextField.getAction().equals(ActionType.JAIL)){ game.getCurrent_player().setStunTurns(4);game.setActionMessage(0);}
        //     else if(nextField.getAction().equals(ActionType.DICE) && nextPos==26){ nextPos = 53; rep = true;game.setActionMessage(2);}
        //     else if(nextField.getAction().equals(ActionType.DICE) && nextPos==53){ nextPos = 26;rep = true;game.setActionMessage(2);}
        //     else if(nextField.getAction().equals(ActionType.BRIDGE) && nextPos==6) { nextPos = 12;rep = true;game.setActionMessage(3);}
        //     else if(nextField.getAction().equals(ActionType.BRIDGE) && nextPos==12) { nextPos = 6;rep = true;game.setActionMessage(3);} 
        //     else if(nextField.getAction().equals(ActionType.GOOSE)){ nextPos = nextGoose(nextField, game); rep = true; game.setActionMessage(1);}
        // }

        if (nextPos>63) nextPos = -(nextPos -63 -63);

        if(nextPos == 63){
            // game.setWinner(game.getCurrent_player());
            // game.setEndTime(LocalDateTime.now());
            // game.setStatus(GameStatus.FINISHED);
            // game.getCurrent_player().getWon_games().add(game);
            game.setTurn_state(TurnState.FINISHED);
        }


        return nextPos;
    }

    private static void movePiece2(Integer nextPos, GamePiece piece, Game game){
        BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());

        if (nextField.getAction() != null){
            if(nextField.getAction().equals(ActionType.DEATH)){ nextField = nextField.getNext_field(); }
            else if(nextField.getAction().equals(ActionType.INN)){ game.getCurrent_player().setStunTurns(1); game.setActionMessage(0);}
            else if(nextField.getAction().equals(ActionType.WELL)){ game.getCurrent_player().setStunTurns(2);game.setActionMessage(0);}
            else if(nextField.getAction().equals(ActionType.MAZE)){ game.getCurrent_player().setStunTurns(3);game.setActionMessage(0);}
            else if(nextField.getAction().equals(ActionType.JAIL)){ game.getCurrent_player().setStunTurns(4);game.setActionMessage(0);}
            else if(nextField.getAction().equals(ActionType.DICE)){ nextField = nextField.getNext_field(); rep = true;game.setActionMessage(2);}
            else if(nextField.getAction().equals(ActionType.BRIDGE)) { nextField = nextField.getNext_field();rep = true;game.setActionMessage(3);}
            else if(nextField.getAction().equals(ActionType.GOOSE)){ nextField = nextField.getNext_field(); rep = true; game.setActionMessage(1);}
        }

        piece.getField().getListGamesPiecesPerBoardField().remove(piece);
        if(nextField.getListGamesPiecesPerBoardField().size()==0){
            nextField.setListGamesPiecesPerBoardField(new ArrayList<GamePiece>());
            nextField.getListGamesPiecesPerBoardField().add(piece);
        }else{
            nextField.getListGamesPiecesPerBoardField().add(piece);
        }
        piece.setField(nextField);
    }

    
    
}