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
package org.springframework.samples.petclinic.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.enums.GameStatus;
import org.springframework.samples.petclinic.enums.GameType;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class GameController
{

    private static final String VIEWS_GAME_CREATE_FORM = "game/createGameForm";
    private static final String VIEWS_GAME_PACHIS = "game/parchis/";
    private static final String VIEWS_GAME_OCA = "game/oca/";

    private final GameService gameService;
    private final UserService userService;

    @ModelAttribute("games")
    public List<Game> findAllCreatedGames() {return this.gameService.findGameByStatus(GameStatus.CREATED);}


    @ModelAttribute("user")
    public User findOwner() {return this.userService.getCurrentUser().get();}


    @Autowired
    public GameController(UserService userService, GameService gameService){
        this.userService = userService;
        this.gameService = gameService;
    }



    /**
     * method for creating a game.
     */
    @GetMapping(value = "/creategame")
    public String initCreationForm( ModelMap model) {
        Game game = new Game();

        model.put("game", game);
        return VIEWS_GAME_CREATE_FORM;
    }

    /**
     *  method for creating a game.
     */
    @PostMapping(value = "/creategame")
    public String processCreationForm(@Valid Game game, @Valid User user, BindingResult result) {

        String new_link;
        System.out.println("New Game created:");

        //System.out.println("game name: " + user.getGamePiece().getTokenColor());
        System.out.println("game password: " + user.getPassword());
        System.out.println("game id: " + game.getGame_id());
        System.out.println("game name: " + game.getName());
        System.out.println("game type: " + game.getType());
        System.out.println("game max: " + game.getMax_player());

        if(user.checkAlreadyCreatedGames())
        {
            return VIEWS_GAME_CREATE_FORM;
        }

        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());
            System.out.println("error 1");

            return VIEWS_GAME_CREATE_FORM;
        }
        else {
            try {
                System.out.println("add created game");
                user.addCreatedGame(game);
                user.createGamePieces(game, user.getTokenColor());

                //saving Game
                //we should also create the appropriate GameBoard here
                game.setCreator(user);
                this.gameService.saveGame(game);

            } catch (Exception ex) {
                System.out.println("exception " + ex.getMessage());

                result.rejectValue("name", "duplicate", "already exists");
                return VIEWS_GAME_CREATE_FORM;
            }
            new_link = (game.getType() == GameType.Parchis) ? VIEWS_GAME_PACHIS : VIEWS_GAME_OCA ;
            new_link = new_link + game.getGame_id();
        }
        return "redirect:/" + new_link;
    }



    }
