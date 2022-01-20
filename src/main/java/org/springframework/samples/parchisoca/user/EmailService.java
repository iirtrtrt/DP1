package org.springframework.samples.parchisoca.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String CONFIRMATION_LINK = "http://localhost:8080/register/confirm?token=";
    private static final String INVITATION_SUBJECT = "Parchis and Oca Invitation";
    private static final String VERIFICATION_SUBJECT = "Complete your account registration";
    private static final String GREETING_TEXT = "Hello there, ";
    private static final String INVITATION_TEXT_1 = "You have been invited by user ";
    private static final String INVITATION_TEXT_2 = " to play a round of Parchis or Oca!";
    private static final String INVITATION_TEXT_END = "Come join us!";
    private static final String VERIFICATION_TEXT_1 = "Please click on the following link to complete your account registration: ";

    public void sendInvitationEmail(String email, String sender) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(INVITATION_SUBJECT);
        message.setText(
                GREETING_TEXT + "\n\n" + INVITATION_TEXT_1 + sender + INVITATION_TEXT_2 + "\n\n" + INVITATION_TEXT_END);
        try {
            mailSender.send(message);

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public void sendTokenMail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(VERIFICATION_SUBJECT);
        message.setText(GREETING_TEXT + "\n\n" + VERIFICATION_TEXT_1 + CONFIRMATION_LINK + token + "\n\n");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
