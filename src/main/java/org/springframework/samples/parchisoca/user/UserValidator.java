package org.springframework.samples.parchisoca.user;

import org.springframework.validation.Errors;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator
{
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
            //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty");

            System.out.println("validator " + user.getPassword());
            System.out.println("validator " + user.getPasswordConfirm());
            System.out.println("validator " + user.getEmail());
            if((user.getPassword().length() < 4 ||  user.getPassword().length() > 30))
            {
                errors.rejectValue("password", "passwordshort", "password is too short! Must have between 4 and 30 characters");
            }
            else if(!user.getPassword().equals(user.getPasswordConfirm()))
            {
                errors.rejectValue("passwordConfirm", "passwordnotmatch", "password does not match");
            }

            /**
            if(patternMatches(user.getEmail(), "^(.+)@(\\S+)$")) {
                //errors.rejectValue("email", "emailInvalid", "this email is not valid");
                errors.rejectValue("passwordConfirm", "passwordnotmatch", "password does not match");
            }
             */

        }

    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
            .matcher(emailAddress)
            .matches();
    }

}
