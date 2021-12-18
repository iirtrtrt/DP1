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
package org.springframework.samples.parchisoca.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private static final String VIEWS_OWNER_CREATE_FORM = "users/createOwnerForm";
    private static final String VIEWS_EDIT_PROFILE_FORM = "users/editProfileForm";
    private static final String VIEWS_ADMIN_HOME = "admins/adminHome";
    private static final String VIEWS_ADMIN_EDIT_PROFILE_FORM = "admins/adminEditProfile";
    private static final String VIEWS_ADMIN_USERS_FORM = "admins/adminUsers";
    private static final String VIEWS_ADMIN_USERS_DETAILS_FORM = "admins/adminUsersDetails";
    private static final String VIEWS_ADMIN_GAMES_FORM = "admins/adminGames";
    private static final String VIEWS_ADMIN_REGISTER_FORM = "admins/adminCreateOwner";

    private final UserService userService;
    private final AuthoritiesService authoritiesService;
    private final GameService gameService;

    @Autowired
    public UserController(UserService userService, AuthoritiesService authoritiesService, GameService gameService) {
        this.userService = userService;
        this.authoritiesService = authoritiesService;
        this.gameService = gameService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder
    public void initUserBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new UserValidator());
    }

    @GetMapping(value = "/register")
    public String register(Map < String, Object > model) {
        User user = new User();
        model.put("user", user);
        return VIEWS_OWNER_CREATE_FORM;
    }

    @PostMapping(value = "/register")
    public String processCreationForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {

            return VIEWS_OWNER_CREATE_FORM;
        } else {
            //creating user
            System.out.println("creating user " + user.getUsername());
            System.out.println("User " + user.getUsername());
            System.out.println("User password " + user.getPassword());

            if (userService.findUser(user.getUsername()).isPresent()) {
                System.out.println("username already taken");
                result.rejectValue("username", "duplicate", "username already taken");
                return VIEWS_OWNER_CREATE_FORM;
            }
            //this.userService.setToken
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/";
        }
    }

    @GetMapping(value = "/editProfile")
    public String editProfile(ModelMap map) {
        User user = userService.getCurrentUser().get();
        System.out.println(user.toString());
        map.put("user", user);
        return VIEWS_EDIT_PROFILE_FORM;
    }

    @PostMapping(value = "/editProfile")
    public String processEditProfileForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_EDIT_PROFILE_FORM;
        } else if (!userService.findUser(user.getUsername()).isPresent()) {
            System.out.println("security breach: user tried to change username");
            return VIEWS_EDIT_PROFILE_FORM;
        } else {
            //updating user profile
            System.out.println("updating user " + user.getUsername());
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/";
        }
    }

    @GetMapping(value = "/admin")
    public String admin(Map < String, Object > model) {
        System.out.println("ADMIN logged in");
        User user = new User();
        model.put("user", user);
        return VIEWS_ADMIN_HOME;
    }

    @GetMapping(value = "/admin/editProfile")
    public String adminEditProfile(ModelMap map) {
        User user = userService.getCurrentUser().get();
        System.out.println(user.toString());
        map.put("user", user);
        return VIEWS_ADMIN_EDIT_PROFILE_FORM;
    }

    @PostMapping(value = "/admin/editProfile")
    public String adminProcessEditProfileForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_ADMIN_EDIT_PROFILE_FORM;
        } else if (!userService.findUser(user.getUsername()).isPresent()) {
            System.out.println("security breach: user tried to change username");
            return VIEWS_ADMIN_EDIT_PROFILE_FORM;
        } else {
            //updating user profile
            System.out.println("updating user " + user.getUsername());
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "admin");
            return "redirect:/";
        }
    }

    @GetMapping(value = "/admin/users")
    public String adminUsers(ModelMap map) {
        User user = userService.getCurrentUser().get();
        System.out.println(user.toString());
        map.put("user", user);
        return VIEWS_ADMIN_USERS_FORM;
    }

    @ModelAttribute("users")
    public List < User > findAUsers() {
        return this.userService.findAllUsers();
    }

    @PostMapping(value = "/admin/users")
    public String adminUsersForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_ADMIN_USERS_FORM;
        } else if (!userService.findUser(user.getUsername()).isPresent()) {
            System.out.println("security breach: user tried to change username");
            return VIEWS_ADMIN_USERS_FORM;
        } else {
            //updating user profile
            System.out.println("updating user " + user.getUsername());
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/admin";
        }
    }

    @GetMapping(value = "/admin/games")
    public String adminGames(ModelMap map) {
        // User user = userService.getCurrentUser().get();
        // System.out.println(user.toString());
        // map.put("user", user);
        return VIEWS_ADMIN_GAMES_FORM;
    }

    @ModelAttribute("games")
    public List < Game > findAllCreatedGames() {
        return this.gameService.findAllGames();
    }

    @PostMapping(value = "/admin/games")
    public String adminGamesForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_ADMIN_GAMES_FORM;
        } else if (!userService.findUser(user.getUsername()).isPresent()) {
            System.out.println("security breach: user tried to change username");
            return VIEWS_ADMIN_GAMES_FORM;
        } else {
            //updating user profile
            System.out.println("updating user " + user.getUsername());
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/";
        }
    }

    @GetMapping(value = "/admin/register")
    public String adminRegister(Map < String, Object > model) {
        User user = new User();
        model.put("user", user);
        return VIEWS_ADMIN_REGISTER_FORM;
    }

    @PostMapping(value = "/admin/register")
    public String adminProcessCreationForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {

            return VIEWS_ADMIN_REGISTER_FORM;
        } else {
            //creating user
            System.out.println("creating user " + user.getUsername());
            System.out.println("User " + user.getUsername());
            System.out.println("User password " + user.getPassword());

            if (userService.findUser(user.getUsername()).isPresent()) {
                System.out.println("username already taken");
                result.rejectValue("username", "duplicate", "username already taken");
                return VIEWS_ADMIN_REGISTER_FORM;
            }
            //this.userService.setToken
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return VIEWS_ADMIN_HOME;
        }
    }

    @GetMapping(value = "/admin/users/details/{username}")
    public String adminUserDetails(ModelMap map, @PathVariable("username") String username) {
        System.out.println("@@@@@@@@@@@@@@@@@@@ " + username + " from detail @@@@@@@2");
        return VIEWS_ADMIN_USERS_DETAILS_FORM;
    }

    @PostMapping(value = "/admin/users/details/{username}")
    public String adminUserDetailsForm(@Valid User user, BindingResult result) {
        return VIEWS_ADMIN_USERS_DETAILS_FORM;
    }

    @GetMapping(value = "/admin/users/delete/{username}")
    public String adminUserDelete(ModelMap map, @PathVariable("username") String username) {
        userService.userDelete(username);
        return "redirect:/admin/users";
    }
}
