package org.springframework.samples.parchisoca.game;


import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game/oca")
public class OcaController {

    @Autowired
    OcaService ocaService;

    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    private static final String VIEWS_GAME = "game/ocaGame";
    //private static final String VIEWS_JOIN_GAME_OCA = "game/oca/join/";

    @Autowired
    public OcaController(GameService gameService, OcaService ocaService, UserService userservice){
        this.ocaService = ocaService;
        this.gameService = gameService;
        this.userService = userservice;
    }


    @GetMapping(value = "{gameid}")
    public String initCanvasForm(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        Game game = this.gameService.findGamebyID(gameid).get();

        ocaService.initGameBoard(game);

        System.out.println("game width:  " + game.getGameboard().getWidth());
        System.out.println("game height:  " + game.getGameboard().getHeight());

        model.put("game",game);
        gameService.saveGame(game);
        return VIEWS_GAME;
        //return "redirect:/" + VIEWS_JOIN_GAME_OCA + gameid;
    
    }
    //@GetMapping(value = "/join/{gameid}")
    //public String joinOca(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        //response.addHeader("Refresh","1");
        //check if this is the current user
        //Optional<Game> gameOptional = this.gameService.findGamebyID(gameid);
        //Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        //ocaService.handleState(game);
        //System.out.println("Turn_State before addAttribute:" + game.getTurn_state());                
        //model.put("game",game);
        //model.addAttribute("currentuser",  userService.getCurrentUser().get());

        //System.out.println("Turn_State before view:" + game.getTurn_state());
        //gameService.saveGame(game);
    //    return VIEWS_GAME;
    //}
}
