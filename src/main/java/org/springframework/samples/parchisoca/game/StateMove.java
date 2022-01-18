package org.springframework.samples.parchisoca.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.stereotype.Component;

import java.awt.Color;


@Component
public class StateMove {


    private static final Logger logger = LogManager.getLogger(StateMove.class);

    private static Boolean kick = false;

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
        game.setTurn_state(TurnState.NEXT);
        kick = false;
        GamePiece selec = getMovingPiece(game);
        // Moves piece from home if possible
        Parchis parchisBoard = (Parchis) game.getGameboard();

        if (parchisBoard.getOptions().get(0).getText().equals(Option.MOVE_HOME)) {
            BoardField dependant = null;
            for (GamePiece piece: game.getCurrent_player().getGamePieces()) {
                if (piece.getField() == null) {
                    if (piece.getTokenColor().equals(Color.GREEN)) dependant = boardFieldService.find(56, game.getGameboard());   
                    else if (piece.getTokenColor().equals(Color.RED)) dependant = boardFieldService.find(39, game.getGameboard());
                    else if (piece.getTokenColor().equals(Color.BLUE)) dependant = boardFieldService.find(22, game.getGameboard());
                    else if (piece.getTokenColor().equals(Color.YELLOW)) dependant = boardFieldService.find(5, game.getGameboard());
                    kickHomePiece(dependant, piece, game);
                    dependant.getListGamesPiecesPerBoardField().add(piece);
                    piece.setField(dependant);
                    break;
                }
            }
            if (kick==true) game.setTurn_state(TurnState.CHOOSEEXTRA);
            //Normal movement
        } else if (game.getDice() != 6 && !parchisBoard.getOptions().get(0).getText().equals(Option.PASS)) {

            Integer nextPos =  calcPosition(selec, game.getDice(), game);
            kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
            movePiece(nextPos, selec, game);
            if (kick == true){
                game.setTurn_state(TurnState.CHOOSEEXTRA);
                // parchisService.handleState(game);
                // return;
            }

        //If dice = 6 normal movement + repeate turn
        } else if (game.getDice() == 6) {

            if (parchisBoard.getOptions().get(0).getText().equals(Option.REPEAT)) {
                game.setTurn_state(TurnState.INIT);
                // parchisService.handleState(game);
                // return;
            } else {

                if (parchisBoard.getOptions().get(0).getText().equals(Option.LOOSE)){
                    List<GamePiece> gamePieces = (game.getCurrent_player().getGamePieces());
                    Collections.shuffle(gamePieces);
                    for (GamePiece piece: gamePieces){
                        if (piece.getField() != null){
                            piece.getField().getListGamesPiecesPerBoardField().remove(piece);
                            piece.setField(null);
                            //game.setTurn_state(TurnState.NEXT);
                            // parchisService.handleState(game);
                            break; //Todo this only breaks the for loop - is that correct?
                        }
                    }
                }else{
                    Integer reps = parchisBoard.getRepetitions();
                    Integer nextPos =  calcPosition(selec, game.getDice(), game);
                    kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
                    movePiece(nextPos, selec, game);
                    if(reps==null){
                      parchisBoard.setRepetitions(1);
                    } else{
                      parchisBoard.setRepetitions(reps+1);
                    }
                    if(kick == true){
                        game.setTurn_state(TurnState.CHOOSEEXTRA);
                        // parchisService.handleState(game);
                        // return;
                    }else{
                        game.setTurn_state(TurnState.INIT);
                        // parchisService.handleState(game); 
                        // return; 
                    }
                }
            }
        }
        parchisService.handleState(game);

    }

    public static GamePiece getMovingPiece (Game game){
        BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
        GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
        for (Option opt: ((Parchis) game.getGameboard()).options) {
            if (opt.getChoosen()) {
                game.addToHistoryBoard(game.getCurrent_player().getFirstname() + ": " + opt.getText());
                fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
                break;
            }
        }
        for (GamePiece piece: game.getCurrent_player().getGamePieces()) {
            if (piece.getField() == fieldSelec){
                selec = piece;  
                break;
            } 
            
        }
        return selec;
    }

    public static void kickPiece (BoardField field, GamePiece piece, Game game){
        if (field.getListGamesPiecesPerBoardField().size()==1 && !(field.getNumber()== 5 || field.getNumber()== 12
        || field.getNumber()== 17 || field.getNumber()== 22 || field.getNumber()== 29 || field.getNumber()== 34 ||
        field.getNumber()== 39 || field.getNumber()== 46 || field.getNumber()== 51 || field.getNumber()== 56 || field.getNumber()== 63 || field.getNumber()== 68 ||
        field.getListGamesPiecesPerBoardField().get(0).getTokenColor().equals(piece.getTokenColor()))){
            GamePiece pieceInField = field.getListGamesPiecesPerBoardField().get(0);
            if(!pieceInField.getTokenColor().equals(piece.getTokenColor())){
                pieceInField.setField(null); 
                field.getListGamesPiecesPerBoardField().remove(pieceInField);
                kick = true;
                return;
            }    
        }
    }

    public static void kickHomePiece(BoardField field, GamePiece myPiece, Game game){
        Color color = myPiece.getTokenColor();
        for(GamePiece piece: field.getListGamesPiecesPerBoardField()){
            if(!piece.getTokenColor().equals(color)){
                piece.setField(null);
                field.getListGamesPiecesPerBoardField().remove(piece);
                kick = true;
                break;
            }
        }
    }


    public static Integer calcPosition(GamePiece piece, Integer moves, Game game){
        Integer x = piece.getField().getNext_field().getNumber();
        Integer nextPos =  x+moves-1;
        if(nextPos != 68) nextPos = nextPos%68;
        else if(nextPos>= 1 && nextPos<= 6 && piece.getField().getNumber()<=68 && piece.getField().getNumber()>=48 && piece.getTokenColor().equals(Color.YELLOW) ) nextPos = nextPos + 168-1;
        else if(nextPos<piece.getField().getNumber() && piece.getField().getNumber()<=51 && piece.getField().getNumber()>=31 && piece.getTokenColor().equals(Color.GREEN) ) nextPos = nextPos - 51 + 151-1;
        else if(nextPos>= 35 && nextPos<= 40 && piece.getField().getNumber()<=34 && piece.getField().getNumber()>=14 && piece.getTokenColor().equals(Color.RED) ) nextPos = nextPos - 34 + 134-1;
        else if(nextPos>= 18 && nextPos<= 23 && ((piece.getField().getNumber()<=17 && piece.getField().getNumber()>=12) || (piece.getField().getNumber()<=65 && piece.getField().getNumber()>=68)) && piece.getTokenColor().equals(Color.BLUE) ) nextPos = nextPos - 17 + 117-1;
        else if(x>=69 && x<200){ nextPos = x+moves-1; }

        //Calculates if there are 2 pieces in a same field in the fields between the actual position of the piece and the supposed next position
        if(nextPos <= 68){
            for(int i = piece.getField().getNext_field().getNumber(); i<=nextPos ;i++){
            BoardField midField = boardFieldService.find(i, game.getGameboard());
            if (midField.getListGamesPiecesPerBoardField().size()==2){
                nextPos = i-1; 
                break;
                } 
            }  
        }else{
            Integer endfor = 68;
            if (piece.getTokenColor().equals(Color.GREEN)) endfor = 51;
            else if (piece.getTokenColor().equals(Color.RED)) endfor = 34;
            else if (piece.getTokenColor().equals(Color.BLUE)) endfor = 17;
            Integer startfor = 168;
            if (piece.getTokenColor().equals(Color.GREEN)) startfor = 151;
            else if (piece.getTokenColor().equals(Color.RED)) startfor = 134;
            else if (piece.getTokenColor().equals(Color.BLUE)) startfor = 117;

            for(int i = piece.getField().getNext_field().getNumber(); i<=endfor ;i++){
                BoardField midField = boardFieldService.find(i, game.getGameboard());
                if (midField.getListGamesPiecesPerBoardField().size()==2){
                    nextPos = i-1;
                    break;
                } 
            }  

            for(int i = startfor; i<=nextPos ;i++){
                BoardField midField = boardFieldService.find(i, game.getGameboard());
                if (midField.getListGamesPiecesPerBoardField().size()==2){
                    nextPos = i-1; 
                    break;
                } 
            } 
        }

        logger.info("Next position " + Integer.toString(nextPos));
        return nextPos;
    }

    public static void movePiece (Integer nextPos, GamePiece piece, Game game) {
        BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
            piece.getField().getListGamesPiecesPerBoardField().remove(piece);
            if(nextField.getListGamesPiecesPerBoardField().size()==0){
                nextField.setListGamesPiecesPerBoardField(new ArrayList<GamePiece>());
                nextField.getListGamesPiecesPerBoardField().add(piece);
            }else{
                GamePiece pieceInField = nextField.getListGamesPiecesPerBoardField().get(0);
                nextField.setListGamesPiecesPerBoardField(new ArrayList<GamePiece>());
                nextField.getListGamesPiecesPerBoardField().add(pieceInField);
                nextField.getListGamesPiecesPerBoardField().add(piece);
            }
            piece.setField(nextField);

        //}

    }
}
