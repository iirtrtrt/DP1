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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;


@Controller
public class GameController
{
    private static final String VIEWS_GAME_CREATE_FORM = "game/createGameForm";
    private final UserService userService;

    public GameController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(value = "/creategame")
    public String initCreationForm(User user, ModelMap model) {
        System.out.println("hello there");
        Game game = new Game();

        Optional<User> current_user = userService.getCurrentUser();
        if(current_user.isPresent())
        {
            User current_user_present = current_user.get();
            current_user_present.addCreatedGame(game);
            game.setCreator(current_user_present);
        }
        else
            System.out.println("ERROR: User not found");

        model.put("game", game);
        return VIEWS_GAME_CREATE_FORM;
    }

    @PostMapping(value = "/creategame")
    public String processCreationForm(@Valid Game game, BindingResult result) {

        System.out.println("game: ");
        System.out.println("game: " + game.getName();
        System.out.println("game: " + game.getType());
        System.out.println("game: " + game.getMax_player());
        System.out.println("game: " + game.getTokenColor().toString());

        if (result.hasErrors()) {

            System.out.println(result.getFieldErrors());

            return VIEWS_GAME_CREATE_FORM;
        }

        System.out.println("You made a post request!");
        return VIEWS_GAME_CREATE_FORM;
    }



}
