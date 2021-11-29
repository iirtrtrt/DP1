package org.springframework.samples.parchisoca.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class InvitationController {

    private static final String VIEWS_INVITATION_FORM = "users/invitationForm";

    @Autowired
    UserService userService;

    @ModelAttribute("users")
    public List<User> populateUsersWithEmail() {return this.userService.findAllUsersWithEmail();}

    public InvitationController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/invite")
    public String viewInvitationForm()
    {
        return VIEWS_INVITATION_FORM;
    }

}
