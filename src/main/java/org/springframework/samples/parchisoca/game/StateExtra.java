package org.springframework.samples.parchisoca.game;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.stereotype.Component;

@Component
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
        Integer nextPos =  StateMove.calcPosition(selec, 20);
        StateMove.kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
        StateMove.movePiece(nextPos, selec, game);
        Integer reps = parchisBoard.getRepetitions();
        if(game.getDice()==6){
            game.setTurn_state(TurnState.INIT);
        } else{
            game.setTurn_state(TurnState.NEXT);
        }
        parchisService.handleState(game);
    }

}
