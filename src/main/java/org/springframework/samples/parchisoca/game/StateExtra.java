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
        if(game.getGameboard().getOptions().get(0).getText()==Option.PASS){

        }else{
            
            GamePiece selec = StateMove.getMovingPiece(game);
            Integer nextPos= 0;
        
            if(parchisBoard.extraAction == false){
                nextPos =  StateMove.calcPosition(selec, 10, game);
            }else{
                nextPos =  StateMove.calcPosition(selec, 20, game);
            }
            StateMove.kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
            StateMove.movePiece(nextPos, selec, game);
        }
        

        if(game.getDice()==6){
            game.setTurn_state(TurnState.INIT);
        } else{
            game.setTurn_state(TurnState.NEXT);
        }
        parchisBoard.setExtraAction(true);
        parchisService.handleState(game);
    }

    

}
