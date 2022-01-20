package org.springframework.samples.parchisoca.model.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator {

    private static final Logger logger = LogManager.getLogger(UserValidator.class);

    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        try {
            User user = (User) obj;
            logger.error("in da validator");

            if (user != null) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty", "password cannot be empty");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "empty",
                        "password cannot be empty");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "emailInvalid", "this email is not valid");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty", "First name cannot be empty");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty", "Last name cannot be empty");
                logger.error("in da validator 2");

                if (user.getUsername().length() < 4) {
                    logger.error("username is too short! Must have between 4 and 30 characters");
                    errors.rejectValue("username", "usernameshort",
                            "username is too short! Must have between 4 and 30 characters");
                } else if ((user.getPassword().length() < 4 || user.getPassword().length() > 30)
                        && !user.getPassword().isEmpty()) {
                    logger.error("password is too short! Must have between 4 and 30 characters");
                    errors.rejectValue("password", "passwordshort",
                            "password is too short! Must have between 4 and 30 characters");
                } else if (!user.getPassword().equals(user.getPasswordConfirm())) {
                    logger.error("password does not match");
                    errors.rejectValue("passwordConfirm", "passwordnotmatch", "password does not match");
                } else if (!patternMatches(user.getEmail(), "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") && !user.getEmail().isEmpty()) {
                    logger.error("this email is not valid");
                    errors.rejectValue("email", "emailInvalid", "this email is not valid");
                }

            }
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());

        }
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
