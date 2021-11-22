package org.springframework.samples.parchisoca.game;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OcaController {

    @Autowired
    OcaService ocaService;

    @Autowired
    GameService gameService;

    private static final String VIEWS_GAME = "game/ocaGame";

    @Autowired
    public OcaController(GameService gameService, OcaService ocaService){
        this.ocaService = ocaService;
        this.gameService = gameService;
    }


    @GetMapping(value = "/game/oca/{gameid}")
    public String initCanvasForm(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        Game game = this.gameService.findGamebyID(gameid).get();

        ocaService.initGameBoard(game);

        System.out.println("game width:  " + game.getGameboard().getWidth());
        System.out.println("game height:  " + game.getGameboard().getHeight());

        model.put("game",game);
        return VIEWS_GAME;
    }
}
