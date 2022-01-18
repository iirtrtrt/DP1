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
                    dependant.getListGamesPiecesPerBoardField().add(piece);
                    piece.setField(dependant);

                    break;
                }
            }
            //Normal movement
        } else if (game.getDice() != 6 && !parchisBoard.getOptions().get(0).getText().equals(Option.PASS)) {

            Integer nextPos =  calcPosition(selec, game.getDice());
            kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
            movePiece(nextPos, selec, game);


        //If dice = 6 normal movement + repeate turn
        } else if (game.getDice() == 6) {

            if (parchisBoard.getOptions().get(0).getText().equals(Option.REPEAT)) {
                game.setTurn_state(TurnState.INIT);
                parchisService.handleState(game);
                return;
            } else {

                if (parchisBoard.getOptions().get(0).getText().equals(Option.LOOSE)){
                    List<GamePiece> gamePieces = (game.getCurrent_player().getGamePieces());
                    Collections.shuffle(gamePieces);
                    for (GamePiece piece: gamePieces){
                        if (piece.getField() != null){
                            piece.getField().getListGamesPiecesPerBoardField().remove(piece);
                            piece.setField(null);
                            game.setTurn_state(TurnState.NEXT);
                            parchisService.handleState(game);
                            break; //Todo this only breaks the for loop - is that correct?
                        }
                    }
                }else{
                    Integer reps = parchisBoard.getRepetitions();
                    Integer nextPos =  calcPosition(selec, game.getDice());
                    kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
                    movePiece(nextPos, selec, game);
                    if(reps==null){
                      parchisBoard.setRepetitions(1);
                    } else{
                      parchisBoard.setRepetitions(reps+1);
                    }
                    
                    game.setTurn_state(TurnState.INIT);
                    parchisService.handleState(game);
                    return;
                }
            }
        }
        game.setTurn_state(TurnState.NEXT);
        parchisService.handleState(game);

    }

    public static GamePiece getMovingPiece (Game game){
        BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
        GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
        for (Option opt: ((Parchis) game.getGameboard()).options) {
            if (opt.getChoosen()) {
                logger.info("The Choice is: " + opt.getText());
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
                game.setTurn_state(TurnState.CHOOSEEXTRA);
                parchisService.handleState(game);
                return;
            }    
        }
    }

    // private static void movePiece20 (Game game){
    //     StateChoosePlay.optionCreator(game.getCurrent_player().getGamePieces(), game);
    //     GamePiece selec = getMovingPiece(game);
    //     Integer nextPos =  calcPosition(selec, game.getDice());
    //     kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
    //     movePiece(nextPos, selec, game);
    // }

    public static Integer calcPosition(GamePiece piece, Integer moves){
        Integer x = piece.getField().getNext_field().getNumber();
        Integer nextPos =  x+moves-1;
        if(nextPos != 68) nextPos = nextPos%68;
        if(nextPos>= 1 && nextPos<= 6 && piece.getField().getNumber()<=68 && piece.getField().getNumber()>=48 && piece.getTokenColor().equals(Color.YELLOW) ) nextPos = nextPos + 168-1;
        else if(nextPos>= 52 && nextPos<= 57 && piece.getField().getNumber()<=51 && piece.getField().getNumber()>=31 && piece.getTokenColor().equals(Color.GREEN) ) nextPos = nextPos - 51 + 151-1;
        else if(nextPos>= 35 && nextPos<= 40 && piece.getField().getNumber()<=34 && piece.getField().getNumber()>=14 && piece.getTokenColor().equals(Color.RED) ) nextPos = nextPos - 34 + 134-1;
        else if(nextPos>= 18 && nextPos<= 23 && ((piece.getField().getNumber()<=17 && piece.getField().getNumber()>=12) || (piece.getField().getNumber()<=65 && piece.getField().getNumber()>=68)) && piece.getTokenColor().equals(Color.BLUE) ) nextPos = nextPos - 17 + 117-1;
        else if(x>=69 && x<200){ nextPos = x+moves-1; }
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
