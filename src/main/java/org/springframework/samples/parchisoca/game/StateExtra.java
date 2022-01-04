package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;

public class StateExtra {

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
        Parchis parchisBoard = (Parchis) game.getGameboard();
        GamePiece selec = StateMove.getMovingPiece(game);
        Integer nextPos =  StateMove.calcPosition(selec, game.getDice());
        StateMove.kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
        StateMove.movePiece(nextPos, selec, game);
        if(game.getDice()==6){
            Integer reps = parchisBoard.getRepetitions();
            if(reps==null){
                parchisBoard.setRepetitions(1);
            } else{
                parchisBoard.setRepetitions(reps+1);
            }
            
            game.setTurn_state(TurnState.INIT);
        }else{
            game.setTurn_state(TurnState.NEXT);
        }
        parchisService.handleState(game);
    }

}
