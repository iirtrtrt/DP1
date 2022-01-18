package org.springframework.samples.parchisoca.game;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.stereotype.Component;


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

        GamePiece selec = game.getCurrent_player().getGamePieces().get(0);

        Integer nextPos =  calcPosition2(selec, game.getDice(), game);
        movePiece2(nextPos, selec, game);
        if(game.getTurn_state()==TurnState.FINISHED){
            ocaService.handleState(game);
        }
        else if(rep == true){
            rep = false;
            game.setTurn_state(TurnState.ROLLDICE);
            ocaService.handleState(game);
        }else{
            game.setTurn_state(TurnState.NEXT);
            ocaService.handleState(game);
        }


    }

    private static Integer calcPosition2 (GamePiece piece, Integer moves, Game game){
        Integer x = piece.getField().getNumber();
        Integer nextPos =  (x+moves);
        if (nextPos>63) nextPos = -(nextPos -63 -63);

        if(nextPos == 63){
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
