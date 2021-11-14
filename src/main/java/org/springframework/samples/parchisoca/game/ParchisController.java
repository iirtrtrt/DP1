package org.springframework.samples.parchisoca.game;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.Random;

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
        
        model.put("game",game);
        return VIEWS_GAME;
    }


    @PostMapping(value = "/{gameid}")
    public String processDice(BindingResult result, @PathVariable("gameid") int gameid) {

        System.out.println("dice was clicked");
        Optional<Game> gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        game.rollDice();

        String new_link = "game/parchis/join/" + game.getGame_id();
        System.out.println("redirecting to" + new_link);
        return "redirect:/" + new_link;
        
        //model.put("game",game);
        //return VIEWS_GAME;
    }

    





    }
