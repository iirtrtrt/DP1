package org.springframework.samples.petclinic.game;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ParchisController {

    @Autowired
    ParchisService parchisService;

    //Not sure if this is ok
    GameService gameservice;

    private static final String VIEWS_GAME = "game/newgame";


    @GetMapping(value = "/game/parchis/{gameid}")
    public String initCanvasForm(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        //response.addHeader("Refresh","1"); 

        
        //gameservice = new GameService(gameRepository)
        //Game game = this.gameservice.findGamebyID(gameid).get();

        //Todo should not be hard coded
        Parchis new_game = new Parchis();
        new_game.background = "resources/images/background_board.jpg";
        new_game.height = 800;
        new_game.width = 800;

        //Create Game fields
        new_game.fields = new ArrayList<BoardField>();
        parchisService.createGameFields(new_game.fields);

        //game pieces 
        //Game game = gameservice.findGamebyID(game_id).get();

        model.put("parchis",new_game);
        return VIEWS_GAME;
    }
    
}
