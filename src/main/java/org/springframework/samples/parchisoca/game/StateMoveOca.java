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
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.stereotype.Component;

import java.awt.Color;


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

        // Oca ocaBoard =(Oca) game.getGameboard();
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
        Integer x = piece.getField().getNext_field().getNumber();
        Integer nextPos =  (x+moves-1);
        BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
        if (nextField.getAction() != null){
            if(nextField.getAction().equals(ActionType.DEATH)){ nextPos = 0;}
            else if(nextField.getAction().equals(ActionType.INN)){ game.getCurrent_player().setStunTurns(1);}
            else if(nextField.getAction().equals(ActionType.WELL)){ game.getCurrent_player().setStunTurns(2);}
            else if(nextField.getAction().equals(ActionType.MAZE)){ game.getCurrent_player().setStunTurns(3);}
            else if(nextField.getAction().equals(ActionType.JAIL)){ game.getCurrent_player().setStunTurns(4);}
            else if(nextField.getAction().equals(ActionType.DICE) && nextPos==26){ nextPos = 53; rep = true;}
            else if(nextField.getAction().equals(ActionType.DICE) && nextPos==53){ nextPos = 26;rep = true;}
            else if(nextField.getAction().equals(ActionType.BRIDGE) && nextPos==6) { nextPos = 12;rep = true;}
            else if(nextField.getAction().equals(ActionType.BRIDGE) && nextPos==12) { nextPos = 6;rep = true;} 
            else if(nextField.getAction().equals(ActionType.GOOSE)){ nextPos = nextGoose(nextField, game); rep = true;}
        }
        return nextPos;
    }

    private static void movePiece2(Integer nextPos, GamePiece piece, Game game){
        BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
        piece.getField().getListGamesPiecesPerBoardField().remove(piece);
        nextField.getListGamesPiecesPerBoardField().add(piece);
        piece.setField(nextField);
    }

    private static Integer nextGoose(BoardField actualGoose, Game game){
        Integer nextGoose = 1;
        List<BoardField> fields = game.getGameboard().getFields();
        List<Integer> goosPos = new ArrayList<Integer>();
        Map<BoardField, Integer> nextPositions = new HashMap<BoardField, Integer>(); 
        for(BoardField field : fields){
            if(field.getAction() != null){
                if(field.getAction().equals(ActionType.GOOSE)){
                    goosPos.add(field.getNumber());
                }
            }
        }
        Collections.sort(goosPos);

        List<BoardField> sortedGooses = new ArrayList<BoardField>();

        for(Integer i : goosPos){   
            sortedGooses.add(boardFieldService.find(i, game.getGameboard()));
        }

        goosPos.add(63);
        goosPos.remove(0);

        
        

        


        for(int i = 0; i<sortedGooses.size(); i++){
            nextPositions.put(sortedGooses.get(i), goosPos.get(i));
        }   
        nextGoose = nextPositions.get(actualGoose);

        return nextGoose;
    }
    
}