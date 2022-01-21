package org.springframework.samples.parchisoca.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

@Controller
public class InvitationController {

    private static final String VIEWS_INVITATION_FORM = "users/invitationForm";

    @Transient
    private static final Logger logger = LoggerFactory.getLogger(InvitationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    public EmailService emailService;

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
    public String viewInvitationForm(ModelMap modelMap, HttpServletResponse response) {
        response.addHeader("Refresh", "5");
        return VIEWS_INVITATION_FORM;
    }

    @GetMapping(value = "/invite/{username}")
    public String sendInvitationEmail(@PathVariable String username) {
        Optional<User> optional = this.userService.findUser(username);

        if (optional.isEmpty())
            logger.error("user not found");

        User user = optional.get();
        this.emailService.sendInvitationEmail(user.getEmail(), this.userService.getCurrentUser().get().getUsername());
        return VIEWS_INVITATION_FORM;
    }
}
