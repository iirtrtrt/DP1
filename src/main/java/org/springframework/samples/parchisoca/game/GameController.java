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
package org.springframework.samples.parchisoca.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.ColorFormatter;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.samples.parchisoca.util.ColorWrapper;
import org.springframework.samples.parchisoca.util.Error;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/game")
public class GameController {

    private static final String VIEWS_GAME_CREATE_FORM = "game/createGameForm";
    private static final String VIEWS_GAME_PACHIS = "game/parchis/";
    private static final String VIEWS_GAME_OCA = "game/oca/";

    private static final String VIEWS_JOIN_GAME = "game/joinGameForm";
    private static final String VIEWS_JOIN_GAME_PACHIS = "game/parchis/join/";
    private static final String VIEWS_JOIN_GAME_OCA = "game/oca/join/   ";

    @Autowired
    private final GameService gameService;

    @Autowired
    private final UserService userService;

    @ModelAttribute("games")
    public List<Game> findAllCreatedGames() {
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
        Game game = new Game();
        model.put("game", game);
        return VIEWS_GAME_CREATE_FORM;
    }

    @GetMapping(value = "/join")
    public String joinGame(ModelMap model) {
        return VIEWS_JOIN_GAME;
    }



    @PostMapping(value = "/join/Parchis/{gameID}")
    public String joinParchisGame(@ModelAttribute("colorWrapper") ColorWrapper colorWrapper, BindingResult bindingResult, @Valid User user, @PathVariable("gameID") int gameID, RedirectAttributes redirectAttributes) {

        Optional<Game> opt_game = gameService.findGamebyID(gameID);
        System.out.println("Game: " + gameID);

        if (bindingResult.hasErrors()) {
            System.out.println("ERROR: Binding has errors!");
            return VIEWS_JOIN_GAME;
        }



        if (opt_game.isPresent()) {
            Game game = opt_game.get();

            Color color = ColorFormatter.parseString(colorWrapper.getColorName());
            if(this.gameService.checkUserAlreadyinGame(user))
            {
                System.out.println("ERROR: already joined the game!");
                Error error = new Error();
                error.setError_message("You already joined a game!");
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/game/join";
            }
            if(!game.checkMaxAmountPlayers() )
            {
                //TODO show error in field
                System.out.println("ERROR: max amount reached!");
                Error error = new Error();
                error.setError_message("The max amount of players was already reached!");
                redirectAttributes.addFlashAttribute("error", error);

                return VIEWS_JOIN_GAME;
            }
            if(!game.checks(color))
            {
                //TODO show error in field
                Error error = new Error();
                error.setError_message("The color was already chosen!");
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/game/join";
            }

            try {
                game.addUser(user);
                user.addJoinedGame(game);
                System.out.println("creating GamePieces");
                this.gameService.createGamePieces(user, game, color);
                System.out.println("finsished creating GamePieces");


            } catch (Exception e) {
                System.out.println("ERROR: Game has not been created!");
            }

            String new_link = (game.getType() == GameType.Parchis) ? VIEWS_JOIN_GAME_PACHIS : VIEWS_JOIN_GAME_OCA;
            new_link = new_link + game.getGame_id();
            System.out
                .println("new_link" + new_link);

            System.out.println("redirecting to" + new_link);
            return "redirect:/" + new_link;
        }
        System.out.println("ERROR: Game has not been found!");
        return "redirect:/";
    }

    /**
     * method for creating a game.
     */
    @PostMapping(value = "/create")
    public String processCreationForm(@Valid @ModelAttribute(name = "game") Game game,BindingResult result, @Valid User user) {

        String new_link;
        if(this.gameService.gameNameExists(game))
        {
            System.out.println("ERROR: already exists");
            result.rejectValue("name","duplicate", "Already exists!");
            return VIEWS_GAME_CREATE_FORM;
        }

        if (user.checkAlreadyCreatedGames()) {
            System.out.println("ERROR: already created");
            result.rejectValue("name","already_created", "You already created a game!");
            return VIEWS_GAME_CREATE_FORM;
        }

        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());

            return VIEWS_GAME_CREATE_FORM;
        } else {
            try {
                System.out.println("add created game");



                user.addCreatedGame(game);
                System.out.println("creating Gamepieces");
                this.gameService.createGamePieces(user, game, user.getTokenColor());
                //user.createGamePieces(game, user.getTokenColor());

                //saving Game
                //we should also create the appropriate GameBoard here
                game.setCreator(user);
                game.setCurrent_players(user);
                game.setCurrent_player(user);

                this.gameService.saveGame(game);

            } catch (Exception ex) {
                System.out.println("exception " + ex.getMessage());

                result.rejectValue("name", "duplicate", "already exists");
                return VIEWS_GAME_CREATE_FORM;
            }
            new_link = (game.getType() == GameType.Parchis) ? VIEWS_GAME_PACHIS : VIEWS_GAME_OCA;
            new_link = new_link + game.getGame_id();
            System.out
                .println("new_link" + new_link);
        }
        System.out.println("redirecting to" + new_link);
        return "redirect:/" + new_link;
    }

}
