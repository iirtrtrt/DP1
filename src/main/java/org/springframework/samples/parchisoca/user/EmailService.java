package org.springframework.samples.parchisoca.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@Component
public class EmailService
{
    @Autowired
    private JavaMailSender mailSender;

    private static final String INVITATION_SUBJECT = "Parchis and Oca Invitation";
    private static final String VERIFICATION_SUBJECT = "Complete your account registration";
    private static final String GREETING_TEXT = "Hello there, ";
    private static final String INVITATION_TEXT_1 = "You have been invited by user ";
    private static final String INVITATION_TEXT_2 = " to play a round of Parchis or Oca!";
    private static final String INVITATION_TEXT_END = "Come join us!";


    public void sendInvitationEmail(String email, String sender)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(VERIFICATION_SUBJECT);
        message.setText();
        mailSender.send(message);


    }

    public void sendRegistrationEmail(String email)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(INVITATION_SUBJECT);
        mailSender.send(message);


    }
}
