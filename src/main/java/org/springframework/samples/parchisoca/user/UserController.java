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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final EmailService emailService;
    @Autowired
    private final VerificationTokenService verificationTokenService;


    @Transient
    private static final Logger logger = LogManager.getLogger(UserController.class);



    @Autowired
    public UserController(UserService userService, AuthoritiesService authoritiesService, GameService gameService, EmailService emailService, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.authoritiesService = authoritiesService;
        this.gameService = gameService;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
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
    public String processCreationForm(@Valid User user, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            logger.info("result has errors");
            return "redirect:/register";
        } else {
            //creating user
            logger.info("creating user " + user.getUsername());
            logger.info("User password " + user.getPassword());

            if (userService.findUser(user.getUsername()).isPresent()) {
                logger.info("username already taken");
                result.rejectValue("username", "duplicate", "username already taken");
                return VIEWS_OWNER_CREATE_FORM;
            }

            this.userService.saveUser(user);
            VerificationToken token = new VerificationToken(user);
            this.verificationTokenService.save(token);
            logger.info("sending email");
            this.emailService.sendTokenMail(user.getEmail(), token.token);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/";
        }
    }

    @GetMapping("/register/confirm")
    public String confirmMail(@RequestParam("token") String token) {

        logger.info("trying to find token");
        Optional < VerificationToken > optionalVerificationToken = verificationTokenService.findByToken(token);

        optionalVerificationToken.ifPresent(userService::confirmUser);
        logger.info("token found!");

        return "redirect:/";
    }

    @GetMapping(value = "/editProfile")
    public String editProfile(ModelMap map) {
        User user = userService.getCurrentUser().get();
        map.put("user", user);
        return VIEWS_EDIT_PROFILE_FORM;
    }

    @PostMapping(value = "/editProfile")
    public String processEditProfileForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_EDIT_PROFILE_FORM;
        } else if (!userService.findUser(user.getUsername()).isPresent()) {
            logger.warn("security breach: user tried to change username");
            return VIEWS_EDIT_PROFILE_FORM;
        } else {
            //updating user profile
            logger.info("updating user " + user.getUsername());
            user.setEnabled(true);
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/";
        }
    }

    @GetMapping(value = "/admin")
    public String admin(Map < String, Object > model) {
        logger.info("ADMIN logged in");
        User user = new User();
        model.put("user", user);
        return VIEWS_ADMIN_HOME;
    }

    @GetMapping(value = "/admin/editProfile")
    public String adminEditProfile(ModelMap map) {
        User user = userService.getCurrentUser().get();
        logger.info(user.toString());
        map.put("user", user);
        return VIEWS_ADMIN_EDIT_PROFILE_FORM;
    }

    @PostMapping(value = "/admin/editProfile")
    public String adminProcessEditProfileForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_ADMIN_EDIT_PROFILE_FORM;
        } else if (!userService.findUser(user.getUsername()).isPresent()) {
            logger.warn("security breach: user tried to change username");
            return VIEWS_ADMIN_EDIT_PROFILE_FORM;
        } else {
            user.setEnabled(true);
            user.setRole(UserRole.ADMIN);
            //updating user profile
            logger.info("updating user " + user.getUsername());
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "admin");
            return VIEWS_ADMIN_HOME;
        }
    }

    @GetMapping(value = "/admin/users")
    public String adminUsers(ModelMap map) {
        User user = userService.getCurrentUser().get();
        logger.info(user.toString());
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
            logger.warn("security breach: user tried to change username");
            return VIEWS_ADMIN_USERS_FORM;
        } else {
            //updating user profile
            logger.info("updating user " + user.getUsername());
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/admin";
        }
    }

    @GetMapping(value = "/admin/games")
    public String adminGames(ModelMap map) {
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
            logger.warn("security breach: user tried to change username");
            return VIEWS_ADMIN_GAMES_FORM;
        } else {
            //updating user profile
            logger.info("updating user " + user.getUsername());
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
            logger.info("has errors");
            return VIEWS_ADMIN_REGISTER_FORM;
        } else {
            //creating user
            logger.info("creating user " + user.getUsername());
            logger.info("User password " + user.getPassword());

            if (userService.findUser(user.getUsername()).isPresent()) {
                logger.info("username already taken");
                result.rejectValue("username", "duplicate", "username already taken");
                return VIEWS_ADMIN_REGISTER_FORM;
            }
            user.setEnabled(true);
            //this.userService.setToken
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return VIEWS_ADMIN_USERS_FORM;
        }
    }

    @GetMapping(value = "/admin/users/details/{username}")
    public String adminUserDetails(ModelMap map, @PathVariable("username") String username) {
        User user = userService.getSelectedUser(username);
        // TODO: prevent admin from showing the admin change profile page
        if(user.getRole() == UserRole.ADMIN) {
            return VIEWS_ADMIN_USERS_FORM;
        } else {
            logger.info("get get get Username :" + username);
            logger.info(user.toString());
            map.put("user", user);
            return VIEWS_ADMIN_USERS_DETAILS_FORM;
        }
    }

    @PostMapping(value = "/admin/users/details/{username}")
    public String adminUserDetailsForm(@Valid User user, BindingResult result, @PathVariable("username") String username) {
        logger.info("post post post Username :" + username);
        if (result.hasErrors()) {
            return VIEWS_ADMIN_USERS_DETAILS_FORM;
        } else {
            logger.info("updating user " + user.getUsername());
            user.setEnabled(true);
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "player");
            return "redirect:/admin/users";
        }
    }

    @GetMapping(value = "/admin/users/delete/{username}")
    public String adminDeleteUser(ModelMap map, @PathVariable("username") String username) {
        userService.deleteUser(username);
        return "redirect:/admin/users";
    }
}
