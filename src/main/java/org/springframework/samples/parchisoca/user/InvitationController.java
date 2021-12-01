package org.springframework.samples.parchisoca.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.samples.parchisoca.game.OcaController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class InvitationController {

    private static final String VIEWS_INVITATION_FORM = "users/invitationForm";


    @Transient
    private static final Logger logger = LoggerFactory.getLogger(InvitationController.class);

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @ModelAttribute("users")
    public List<User> populateUsersWithEmail() {
        List<User> listUsers = this.userService.findAllUsersWithEmail();
        User myself = this.userService.getCurrentUser().get();
        listUsers.remove(myself);
        return listUsers;
    }

    public InvitationController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }


    @GetMapping(value = "/invite")
    public String viewInvitationForm(ModelMap model, HttpServletResponse response)
    {
        response.addHeader("Refresh", "5");
        //model.put("invitationForm", value)
        return VIEWS_INVITATION_FORM;
    }



    @GetMapping(value = "/invite/{username}")
    public String sendEmail(@PathVariable String username)
    {
        Optional<User> optional = this.userService.findUser(username);
        User currentUser = this.userService.getCurrentUser().get();
        if(optional.isEmpty())
            logger.error("user not found");

        User user = optional.get();
        this.emailService.sendEmail(user.getEmail(), user.getUsername(), currentUser.getEmail(), currentUser.getUsername());
        return VIEWS_INVITATION_FORM;
    }
}
