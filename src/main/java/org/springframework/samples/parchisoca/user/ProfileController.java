package org.springframework.samples.parchisoca.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.Validator;

@Controller
public class ProfileController {

    private static final String VIEWS_EDIT_PROFILE_FORM = "users/editProfileForm";

    private final UserService userService;
    private final AuthoritiesService authoritiesService;

    @Autowired
    public ProfileController(UserService userService, AuthoritiesService authoritiesService) {
        this.userService = userService;
        this.authoritiesService = authoritiesService;
    }

    @InitBinder
    public void initUserBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PasswordValidator());
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
        }
        else if(!userService.findUser(user.getUsername()).isPresent())
        {
            System.out.println("security breach: user tried to change username");
            return VIEWS_EDIT_PROFILE_FORM;
        }
        else {
            //updating user profile
            System.out.println("updating user " + user.getUsername());
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "user");
            return "redirect:/";
        }
    }
}
