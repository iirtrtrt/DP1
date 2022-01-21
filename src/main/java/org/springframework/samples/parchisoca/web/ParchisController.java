package org.springframework.samples.parchisoca.web;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.model.game.Parchis;
import org.springframework.samples.parchisoca.service.GameService;
import org.springframework.samples.parchisoca.service.ParchisService;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/game/parchis")
public class
ParchisController {


    private static final Logger logger = LogManager.getLogger(ParchisController.class);

    @Autowired
    ParchisService parchisService;

    //Not sure if this is ok
    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;


    private static final String VIEWS_GAME = "game/parchisGame";
    private static final String VIEWS_JOIN_GAME_PACHIS = "game/parchis/join/";


    @Autowired
    public ParchisController(GameService gameService, ParchisService parchisService, UserService userservice) {
        this.parchisService = parchisService;
        this.gameService = gameService;
        this.userService = userservice;
    }

    @GetMapping(value = "{gameid}")
    public String initCanvasForm(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        Game game = this.gameService.findGamebyID(gameid).get();
        parchisService.initGameBoard(game);
        return "redirect:/" + VIEWS_JOIN_GAME_PACHIS + gameid;
    }

    @GetMapping(value = "/join/{gameid}")
    public String joinParchis(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        response.addHeader("Refresh","5");
        //check if this is the current user
        Optional < Game > gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);

        parchisService.handleState(game);

        model.addAttribute("game", game);
        model.addAttribute("currentuser", userService.getCurrentUser().get());

        gameService.saveGame(game);
        return VIEWS_GAME;
    }

    @GetMapping(value = "/join/{gameid}/quit")
    public String quitParchis(@PathVariable("gameid") int gameid) {
        this.gameService.quitGame(gameid);
        return "redirect:/";
    }

    @GetMapping(value = "/join/{gameid}/dice")
    public String diceRole(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        //check if this is the current user
        Optional < Game > gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        game.setTurn_state(TurnState.ROLLDICE);
        gameService.saveGame(game);
        return "redirect:/" + VIEWS_JOIN_GAME_PACHIS + gameid;
    }

    @GetMapping(value = "/join/{gameid}/choice/{choiceid}")
    public String processChoice(@PathVariable("gameid") int gameid, @PathVariable("choiceid") int choiceid, ModelMap model, HttpServletResponse response)
    {
        Optional <Game> gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        if(game.getTurn_state().equals(TurnState.DIRECTPASS)){
            game.setTurn_state(TurnState.PASSMOVE);
        }else if(game.getTurn_state().equals(TurnState.CHOOSEEXTRA)){
            game.setTurn_state(TurnState.EXTRA);
        }else{
            game.setTurn_state(TurnState.MOVE);
        }
        for (Option opt: ((Parchis) game.getGameboard()).options) {
            if (opt.getNumber() == choiceid) {
                logger.info("The correct choice has been found");
                opt.setChoosen(true);
            }
        }
        gameService.saveGame(game);

        return "redirect:/" + VIEWS_JOIN_GAME_PACHIS + gameid;
    }
}
