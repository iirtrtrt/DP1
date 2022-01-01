package org.springframework.samples.parchisoca.game;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/game/parchis")
public class ParchisController {


    private static final Logger logger = LogManager.getLogger(ParchisController.class);

    @Autowired
    ParchisService parchisService;

    //Not sure if this is ok
    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    private static final String VIEWS_GAME = "game/newgame";
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

        logger.info("game width:  " + game.getGameboard().getWidth());
        logger.info("game height:  " + game.getGameboard().getHeight());

        //model.put("game",game);
        return "redirect:/" + VIEWS_JOIN_GAME_PACHIS + gameid;
    }

    @GetMapping(value = "/join/{gameid}")
    public String joinParchis(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
       // response.addHeader("Refresh","5");
        //check if this is the current user
        Optional < Game > gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);

        //if(userTurns.size()<game.getMax_player()){
          //  Map<User,Integer> mapa = parchisService.turns(game, userTurns);
            //userTurns=mapa;
        //}
        //else {
          //  Map<User,Integer> mapaOrdenado = userTurns.entrySet().stream()
            //                     .sorted((Map.Entry.<User,Integer>comparingByValue().reversed()))
              //                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2)->e1, LinkedHashMap::new));

            //List<User> turns = mapaOrdenado.keySet().stream().collect(Collectors.toList());
           // System.out.println("El orden sera " + turns);
            parchisService.handleState(game);

        parchisService.handleState(game);

        //}

        //System.out.println("Turn_State before addAttribute:" + game.getTurn_state());
        //System.out.println("Values and Users:" + userTurns);
        //System.out.println("Size of map " + userTurns.size());
        //System.out.println("Number of players " + game.getCurrent_players().size());

        //System.out.println("El usuario/player de ahorita es :" + game.getCurrent_player());

        model.addAttribute("game", game);
        model.addAttribute("currentuser", userService.getCurrentUser().get());

        gameService.saveGame(game);
        return VIEWS_GAME;
    }

    @GetMapping(value = "/join/{gameid}/quit")
    public String quitParchis(@PathVariable("gameid") int gameid) {
        logger.info("quitGame: " + gameid);
        Optional < Game > gameOptional = this.gameService.findGamebyID(gameid);
        Game game = gameOptional.orElseThrow(EntityNotFoundException::new);
        game.setStatus(GameStatus.FINISHED);
        this.gameService.deleteAllGamePieces(game);
        gameService.saveGame(game);
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
