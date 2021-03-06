/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.parchisoca.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.service.GameService;
import org.springframework.samples.parchisoca.model.user.ColorFormatter;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.samples.parchisoca.util.ColorWrapper;
import org.springframework.samples.parchisoca.util.Error;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.awt.*;
import java.util.List;


import java.util.Optional;


@Controller
@RequestMapping("/game")

public class GameController {


    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String VIEWS_GAME_CREATE_FORM = "game/createGameForm";
    private static final String VIEWS_GAME_PACHIS = "game/parchis/";
    private static final String VIEWS_GAME_OCA = "game/oca/";

    private static final String VIEWS_JOIN_GAME = "game/joinGameForm";
    private static final String VIEWS_JOIN_GAME_PACHIS = "game/parchis/join/";
    private static final String VIEWS_JOIN_GAME_OCA = "game/oca/join/";


    private final GameService gameService;


    private final UserService userService;




    @ModelAttribute("games")
    public List <Game> findAllCreatedGames() {
        return this.gameService.findGameByStatus(GameStatus.CREATED);
    }


    @ModelAttribute("user")
    public User findUser() {

        return this.userService.getCurrentUser().get();
    }

    @ModelAttribute("colorWrapper")
    public ColorWrapper setColor() {
        return new ColorWrapper();
    }

    @Autowired
    public GameController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;

    }

    /**
     * method for creating a game.
     */
    @GetMapping(value = "/create")
    public String initCreationForm(ModelMap model) {
        logger.info("initCreationForm");
        Game game = new Game();
        model.put("game", game);
        return VIEWS_GAME_CREATE_FORM;
    }

    @GetMapping(value = "/join")
    public String joinGame(ModelMap model) {
        return VIEWS_JOIN_GAME;
    }

    @PostMapping(value = "/join/Parchis/{gameID}")
    public String joinParchisGame(@ModelAttribute("colorWrapper") ColorWrapper colorWrapper, @Valid User user, BindingResult bindingResult, @PathVariable("gameID") int gameID, RedirectAttributes redirectAttributes)
    {
        Optional < Game > opt_game = gameService.findGamebyID(gameID);
       Error error = new Error();
        logger.info("Game: " + gameID);

        if (bindingResult.hasErrors()) {
            logger.error("ERROR: Binding has errors!");
            return VIEWS_JOIN_GAME;
        }

        if (opt_game.isPresent()) {
            Game game = opt_game.get();


            for(User player : game.getOther_players())
                logger.info("other_players player: " + player.getUsername());

            for(User player : game.getCurrent_players())
                logger.info("before player: " + player.getUsername());


            Color color = ColorFormatter.parseString(colorWrapper.getColorName());
            if (this.gameService.checkUserAlreadyinGame(user)) {
                logger.error("ERROR: already joined the game!");
                error.setError_message("You already joined a game!");
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/game/join";
            }

            if (!game.checkMaxAmountPlayers()) {
                logger.error("amount of players" + game.getCurrent_players().size());
                for(User player : game.getCurrent_players())
                    logger.info("player: " + player.getUsername());
                logger.error("ERROR: max amount reached! max amount is " + game.getMax_player());
                error.setError_message("The max amount of players was already reached!");
                redirectAttributes.addFlashAttribute("error", error);

                return "redirect:/game/join";
            }
            if (!this.gameService.checkColor(game,color)) {
                error.setError_message("The color was already chosen!");
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/game/join";
            }
            try {
                game.addUser(user);
                logger.info("creating GamePieces");
                user.addJoinedGame(game);
                logger.info("creating GamePieces");
                this.gameService.createGamePieces(user, game, color);
                logger.info("finsished creating GamePieces");
            } catch (Exception e) {
                logger.error("ERROR: Game has not been created!");
            }

            String new_link = (game.getType() == GameType.Parchis) ? VIEWS_JOIN_GAME_PACHIS : VIEWS_JOIN_GAME_OCA;
            new_link = new_link + game.getGame_id();

            logger.info("redirecting to" + new_link);
            return "redirect:/" + new_link;
        }
        logger.error("ERROR: Game has not been found!");
        return "redirect:/";
    }

    @PostMapping(value = "/join/Oca/{gameID}")
    public String joinOcaGame(@ModelAttribute("colorWrapper") ColorWrapper colorWrapper, BindingResult bindingResult, @Valid User user, @PathVariable("gameID") int gameID, RedirectAttributes redirectAttributes) {
        Optional < Game > opt_game = gameService.findGamebyID(gameID);
        Error error = new Error();
        logger.info("Game: " + gameID);

        if (bindingResult.hasErrors()) {
            logger.error("ERROR: Binding has errors!");
            return VIEWS_JOIN_GAME;
        }

        if (opt_game.isPresent()) {
            Game game = opt_game.get();

            Color color = ColorFormatter.parseString(colorWrapper.getColorName());
            if (this.gameService.checkUserAlreadyinGame(user)) {
                logger.error("ERROR: already joined the game!");
                error.setError_message("You already joined a game!");
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/game/join";
            }
            if (!game.checkMaxAmountPlayers()) {
                logger.error("ERROR: max amount reached!");
                error.setError_message("The max amount of players was already reached!");
                redirectAttributes.addFlashAttribute("error", error);

                return VIEWS_JOIN_GAME;
            }
            if (!this.gameService.checkColor(game, color)) {
                error.setError_message("The color was already chosen!");
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/game/join";
            }
            try {
                game.addUser(user);
                user.addJoinedGame(game);
                logger.info("creating GamePieces");
                this.gameService.createGamePieces(user, game, color);
                logger.info("finsished creating GamePieces");
            } catch (Exception e) {
                logger.error("ERROR: Game has not been created!");
            }

            String new_link = (game.getType() == GameType.Parchis) ? VIEWS_JOIN_GAME_PACHIS : VIEWS_JOIN_GAME_OCA;
            new_link = new_link + game.getGame_id();
            logger.info("redirecting to" + new_link);
            return "redirect:/" + new_link;
        }
        logger.error("ERROR: Game has not been found!");
        return "redirect:/";
    }

    /**
     * method for creating a game.
     */
    @PostMapping(value = "/create")
    public String processCreationForm(@Valid @ModelAttribute(name = "game") Game game, BindingResult result, @Valid User user) {

        String new_link;
        logger.info("createGame " + game.getName());

        if (result.hasErrors()) {
            return VIEWS_GAME_CREATE_FORM;
        }
        if (this.gameService.gameNameExists(game)) {
            logger.error("ERROR: already exists");
            result.rejectValue("name", "duplicate", "Already exists!");
            return VIEWS_GAME_CREATE_FORM;
        }

        if (user.checkAlreadyCreatedGames()) {
            logger.error("ERROR: already created");
            result.rejectValue("name", "already_created", "You already created a game!");
            return VIEWS_GAME_CREATE_FORM;
        }
        else {
            try {
                logger.info("creating Gamepieces");
                this.gameService.createGamePieces(user, game, user.getTokenColor());
                this.gameService.setPlayersOfGame(game, user);
                this.gameService.saveGame(game);

                //Create AI user if checkbox is clicked
                if(game.isAI()) {

                    User ai = new User();
                    userService.setAI(ai, user);
                    this.gameService.createGamePieces(ai, game, ai.getTokenColor());
                    game.addUser(ai);
                    this.gameService.saveGame(game);
                }
                this.gameService.initGame(game);


            } catch (Exception ex) {
                logger.error("ERROR: " + ex.getMessage());

                result.rejectValue("name", "exception", "An unexpected exception occured");
                return VIEWS_GAME_CREATE_FORM;
            }
            new_link = (game.getType() == GameType.Parchis) ? VIEWS_GAME_PACHIS : VIEWS_GAME_OCA;
            new_link = new_link + game.getGame_id();



        }
        logger.info("redirecting to" + new_link);
        return "redirect:/" + new_link;
    }
}
