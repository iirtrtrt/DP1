package org.springframework.samples.parchisoca.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateChoosePlay {

    private static OptionService optionService;
    @Autowired
    private OptionService optionService_;

    private static BoardFieldService boardFieldService;
    @Autowired
    private BoardFieldService boardFieldService_;




    @PostConstruct
    private void initStaticDao () {
        optionService = this.optionService_;
        boardFieldService = this.boardFieldService_;
    }


    public static void doAction(Game game){
        Parchis parchis = (Parchis) game.getGameboard();
        parchis.options = new ArrayList<>();
        BoardField startField = null;
        Color currentColor = game.getCurrent_player().getGamePieces().get(0).getTokenColor();
        if(currentColor.equals(Color.GREEN)) startField = boardFieldService.find(56, game.getGameboard());
        else if(currentColor.equals(Color.RED)) startField = boardFieldService.find(39, game.getGameboard());
        else if(currentColor.equals(Color.BLUE)) startField = boardFieldService.find(22, game.getGameboard());
        else if(currentColor.equals(Color.YELLOW)) startField = boardFieldService.find(5, game.getGameboard());
        optionCreator(game.getCurrent_player().getGamePieces(), game);
        if(parchis.getOptions().size() == 0){
            if(game.getDice()<5){
               Option op = new Option();
                op.setNumber(1);
                op.setText("Pass turn");
                optionService.saveOption(op);
                parchis.options.add(op);
            } else if (game.getDice() == 5) {
                Option op = new Option();
                op.setNumber(1);
                op.setText("Move piece from home");
                optionService.saveOption(op);
                parchis.options.add(op);
            }else if(game.getDice() == 6){
                Option op = new Option();
                op.setNumber(1);
                op.setText("Repeat turn");
                optionService.saveOption(op);
                parchis.options.add(op);
            }

        }else if(game.getDice()==5 && parchis.getOptions().size() < 4 && startFieldAvailable(startField, game.getCurrent_player().getGamePieces().get(0).getTokenColor() )){ //If this fulfills you have to move a piece from home to start
            parchis.options = new ArrayList<>();
            Option op = new Option();
            op.setNumber(1);
            op.setText("Move piece from home");
            optionService.saveOption(op);
            parchis.options.add(op);
        } else if(game.getDice()==6 && parchis.getRepetitions()!=null){
            if(parchis.getRepetitions()==2){
                parchis.options = new ArrayList<>();
                Option op = new Option();
                op.setNumber(1);
                op.setText("Lose piece");
                optionService.saveOption(op);
                parchis.options.add(op);
            }

        }
    }


    public static void optionCreator(List <GamePiece> pieces, Game game) {
        Parchis parchis = (Parchis) game.getGameboard();
        for (GamePiece piece: pieces) {
            if (piece.getField() !=null) {
                Integer nextPos = calcPosition(piece, game.getDice());
                BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
                Boolean isBlocked = false;
                //Calculates if there are 2 pieces in a same field in the fields between the actual position of the piece and the supposed next position
                if(nextPos <= 68){
                    for(int i = piece.getField().getNext_field().getNumber(); i<=nextPos ;i++){
                    BoardField midField = boardFieldService.find(i, game.getGameboard());
                    if (midField.getListGamesPiecesPerBoardField().size()==2){
                        isBlocked = true; 
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
                        isBlocked = true; 
                        break;
                        } 
                    }  

                    for(int i = startfor; i<=nextPos ;i++){
                        BoardField midField = boardFieldService.find(i, game.getGameboard());
                        if (midField.getListGamesPiecesPerBoardField().size()==2){
                            isBlocked = true; 
                            break;
                            } 
                        } 
                }
                
                //Movement only possible
                // if(!isBlocked && (nextField.getListGamesPiecesPerBoardField().size() ==0 || (nextField.getListGamesPiecesPerBoardField().size()==1 &&
                // (nextField.getListGamesPiecesPerBoardField().get(0).getTokenColor().equals(piece.getTokenColor()) || nextField.getNumber()== 5 || nextField.getNumber()== 12 ||
                // nextField.getNumber()== 17 || nextField.getNumber()== 22 || nextField.getNumber()== 29 || nextField.getNumber()== 34 || nextField.getNumber()== 39 ||
                // nextField.getNumber()== 46 || nextField.getNumber()== 51 || nextField.getNumber()== 56 || nextField.getNumber()== 63 || nextField.getNumber()== 68 ))))
                if(!isBlocked){
                    Integer fieldNumber = piece.getField().getNumber();
                    Option op = new Option();
                    op.setNumber(fieldNumber);
                    op.setText("Move piece in field " + String.valueOf(fieldNumber));
                    optionService.saveOption(op);
                    parchis.options.add(op);
                }
            }
        }
    }

    private static Integer calcPosition(GamePiece piece, Integer moves){
        Integer x = piece.getField().getNext_field().getNumber();
        Integer nextPos =  (x+moves-1)%68;
        if(nextPos>= 1 && nextPos<= 6 && piece.getField().getNumber()<=68 && piece.getField().getNumber()>=48 && piece.getTokenColor().equals(Color.YELLOW) ) nextPos = nextPos - 68 + 168-1;
        else if(nextPos>= 52 && nextPos<= 57 && piece.getField().getNumber()<=51 && piece.getField().getNumber()>=31 && piece.getTokenColor().equals(Color.GREEN) ) nextPos = nextPos%151 - 51 + 151-1;
        else if(nextPos>= 35 && nextPos<= 40 && piece.getField().getNumber()<=34 && piece.getField().getNumber()>=14 && piece.getTokenColor().equals(Color.RED) ) nextPos = nextPos%134 - 34 + 134-1;
        else if(nextPos>= 18 && nextPos<= 23 && ((piece.getField().getNumber()<=17 && piece.getField().getNumber()>=12) || (piece.getField().getNumber()<=65 && piece.getField().getNumber()>=68)) && piece.getTokenColor().equals(Color.BLUE) ) nextPos = nextPos%117 - 17 + 117-1;

        return nextPos;
    }


    private static Boolean startFieldAvailable (BoardField field, Color color){
        Boolean res = false;
        if(field.getListGamesPiecesPerBoardField().size()<2){
            res = true;
        }else{
            for(GamePiece piece: field.getListGamesPiecesPerBoardField()){
                if(!piece.getTokenColor().equals(color)){
                    piece.setField(null);
                    field.getListGamesPiecesPerBoardField().remove(piece);
                    res = true;
                    break;
                }
            }
        }
        return res;
    }


}
