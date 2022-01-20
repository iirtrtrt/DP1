
package org.springframework.samples.parchisoca.web;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.model.game.Oca;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.GamePiece;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.service.GameService;
import org.springframework.samples.parchisoca.service.OcaService;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@RequestMapping("/game/oca")
public class OcaController {


    private static final Logger logger = LoggerFactory.getLogger(OcaController.class);


    @Autowired
    OcaService ocaService;
    @Autowired
    GameService gameService;
    @Autowired
    UserService userService;


    private static final String VIEWS_GAME = "game/ocaGame";
    private static final String VIEWS_JOIN_GAME_OCA = "game/oca/join/";

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
        return "redirect:/" + VIEWS_JOIN_GAME_OCA + gameid;
    }

    @GetMapping(value = "/join/{gameid}")
    public String joinOca(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) throws InterruptedException {
      //  response.addHeader("Refresh", "5");
        Optional < Game > gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        User user  = userService.getCurrentUser().get();

        if(game.getStatus() != GameStatus.FINISHED)
        {
            GamePiece pieces = user.getGamePieces().get(0);
            if(pieces.getField() == null){
                pieces.setField(game.getStartField());
            }
        }

        ocaService.handleState(game);
        userService.saveUser(user, user.getRole());

        model.addAttribute("currentuser", userService.getCurrentUser().get());
        model.put("game",game);

        return VIEWS_GAME;
    }

    @GetMapping(value = "/join/{gameid}/quit")
    public String quitOca(@PathVariable("gameid") int gameid) {
        this.gameService.quitGame(gameid);
        return "redirect:/";
    }

    @GetMapping(value = "/join/{gameid}/dice")
    public String diceRole(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {

        //check if this is the current user
        Optional <Game> gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        game.setTurn_state(TurnState.ROLLDICE);
        gameService.saveGame(game);

        //parchisService.handleState(game);
        ocaService.handleState(game);

        return "redirect:/" + VIEWS_JOIN_GAME_OCA + gameid;
    }
    @GetMapping(value = "/join/{gameid}/choice/{choiceid}")
    public String processChoice(@PathVariable("gameid") int gameid, @PathVariable("choiceid") int choiceid, ModelMap model, HttpServletResponse response) {
        //response.addHeader("Refresh","1");
        //check if this is the current user
        Optional < Game > gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        if(game.getTurn_state().equals(TurnState.DIRECTPASS)){
            game.setTurn_state(TurnState.PASSMOVE);
        }else{
            game.setTurn_state(TurnState.MOVE);
        }
        for (Option opt: ((Oca) game.getGameboard()).options) {
            if (opt.getNumber() == choiceid) {
                opt.setChoosen(true);
            }
        }
        gameService.saveGame(game);

        return "redirect:/" + VIEWS_JOIN_GAME_OCA + gameid;
    }
}
