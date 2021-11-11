package org.springframework.samples.parchisoca.game;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/game/parchis")
public class ParchisController {

    @Autowired
    ParchisService parchisService;

    //Not sure if this is ok
    @Autowired
    GameService gameService;

    private static final String VIEWS_GAME = "game/newgame";

    @Autowired
    public ParchisController(GameService gameService, ParchisService parchisService){
        this.parchisService = parchisService;
        this.gameService = gameService;
    }

    @GetMapping(value = "/{gameid}")
    public String initCanvasForm(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        //response.addHeader("Refresh","1");

        Game game = this.gameService.findGamebyID(gameid).get();

        parchisService.initGameBoard(game);

        System.out.println("game width:  " + game.getGameboard().getWidth());
        System.out.println("game height:  " + game.getGameboard().getHeight());

        model.put("game",game);
        return VIEWS_GAME;
    }

    @GetMapping(value = "/join/{gameid}")
    public String joinParchis(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {

        System.out.println("joinParchis");
        Optional<Game> gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        System.out.println("Fields size:");
        System.out.println(String.valueOf(game.getGameboard().getFields().size()));
        if(game.getGameboard().getFields().size() == 0){
            System.out.println("Size = 0");
        }
        else{
            System.out.println("Size  > 0");
   
        }
        model.put("game",game);
        return VIEWS_GAME;
    }





    }
