package org.springframework.samples.parchisoca.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.validation.Errors;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator
{

    private static final Logger logger = LogManager.getLogger(UserValidator.class);

    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors)
    {
        User user = (User) obj;

        if(user != null)
        {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty");

            logger.debug("validator " + user.getPassword());
            logger.debug("validator " + user.getPasswordConfirm());
            logger.debug("validator " + user.getEmail());

            //password field
            if((user.getPassword().length() < 4 ||  user.getPassword().length() > 30))
            {
                logger.error("password is too short! Must have between 4 and 30 characters");
                errors.rejectValue("password", "passwordshort", "password is too short! Must have between 4 and 30 characters");
            }

            //password confirm field
            if(!user.getPassword().equals(user.getPasswordConfirm()))
            {
                logger.error("password does not match");
                errors.rejectValue("passwordConfirm", "passwordnotmatch", "password does not match");
            }

            //email field
            if(!patternMatches(user.getEmail(), "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
                logger.error("this email is not valid");
                errors.rejectValue("email", "emailInvalid", "this email is not valid");
            }

        }

    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
            .matcher(emailAddress)
            .matches();
    }

}
