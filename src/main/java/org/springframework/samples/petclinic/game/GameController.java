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
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class GameController
{    
    @Autowired
    GameBoardService gameBoardService;

    private static final String VIEWS_GAME_CREATE_FORM = "game/createGameForm";
    private static final String VIEWS_GAME = "game/newgame";
    private final GameService gameService;
    private final UserService userService;

    @ModelAttribute("games")
    public List<Game> findAllGames() {return this.gameService.findAllGames();}


    @ModelAttribute("user")
    public User findOwner() {return this.userService.getCurrentUser().get();}



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


        //model.put("user", current_user_present);
        model.put("game", game);
        return VIEWS_GAME_CREATE_FORM;
    }

    /**
     *  method for creating a game.
     */
    @PostMapping(value = "/creategame")
    public String processCreationForm(@Valid Game game, @Valid User user, BindingResult result) {

        System.out.println("game name: " + user.getTokenColor());
        System.out.println("game password: " + user.getPassword());

        System.out.println("game id: " + game.getGame_id());
        System.out.println("game name: " + game.getName());
        System.out.println("game type: " + game.getType());
        System.out.println("game max: " + game.getMax_player());

        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());

            return VIEWS_GAME_CREATE_FORM;
        }
        else {
            try {
                user.addCreatedGame(game);
                game.setCreator(user);
                this.gameService.saveGame(game);
            } catch (Exception ex) {
                result.rejectValue("name", "duplicate", "already exists");
                return VIEWS_GAME_CREATE_FORM;
            }
        }

        this.gameService.saveGame(game);

        System.out.println("You made a post request!");
        return "redirect:/newgame";
    }


        /**
     * method for creating a game canvas.
     */


    @GetMapping(value = "/newgame")
    public String initCanvasForm( ModelMap model, HttpServletResponse response) {
        //response.addHeader("Refresh","1"); 
        gameBoardService.findById(1).get();
        System.out.println("before model");
        model.put("gameBoard",gameBoardService.findById(1).get());
        System.out.println("viewing GameBoard");
        return VIEWS_GAME;
    }








}
