package org.springframework.samples.parchisoca.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.stereotype.Component;

import java.awt.Color;


@Component
public class StateMoveOca {

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
        Oca ocaBoard =(Oca) game.getGameboard();
        BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
        GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
        BoardField dependant = boardFieldService.find(1, game.getGameboard());
        for (Option opt: ocaBoard.getOptions()) {
            if (opt.getChoosen()) {
                System.out.println("The Choice is: " + opt.getText());
                fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
            }
        }
        selec.setField(dependant); 
        Integer nextPos =  calcPosition2(selec, game.getDice());
        movePiece2(nextPos, selec, game);
        game.setTurn_state(TurnState.NEXT);
        ocaService.handleState(game);
    }

    private static Integer calcPosition2 (GamePiece piece, Integer moves){
        Integer x = piece.getField().getNext_field().getNumber();
        Integer nextPos =  (x+moves-1);
        return nextPos;
    }

    private static void movePiece2(Integer nextPos, GamePiece piece, Game game){
        BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
        piece.getField().getListGamesPiecesPerBoardField().remove(piece);
        nextField.getListGamesPiecesPerBoardField().add(piece);
        piece.setField(nextField);
    }
}