package org.springframework.samples.parchisoca.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.parchisoca.model.Person;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// TODO WIP
public class UserValidatorTest {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldNotValidateWhenUsernameEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.username = "";
        user.firstname = "sam";
        user.lastname = "smith";
        user.password = "12345";
        user.passwordConfirm = "12345";

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
    }

    @Test
    void shouldNotValidateWhenUsernameTooShort() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.username = "s";
        user.firstname = "sam";
        user.lastname = "smith";
        user.password = "12345";
        user.passwordConfirm = "12345";

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
    }

    @Test
    void shouldNotValidateWhenPasswordEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.username = "sami02";
        user.firstname = "sam";
        user.lastname = "smith";
        user.password = "";
        user.passwordConfirm = "";

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
    }

    // TODO doesnt work because our own Validator isnt doing anything in the testcases
    @Disabled
    @Test
    void shouldNotValidateWhenPasswordConfirmEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.username = "sami02";
        user.firstname = "sam";
        user.lastname = "smith";
        user.password = "12345";
        user.passwordConfirm = "";

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        System.out.println(constraintViolations.size());
        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("passwordConfirm");
        //assertThat(violation.getMessage()).isEqualTo("password does not match");
    }

    @Test
    void shouldNotValidateWhenPasswordToShort() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.username = "sami02";
        user.firstname = "sam";
        user.lastname = "smith";
        user.password = "123";
        user.passwordConfirm = "123";

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
    }


    // TODO doesnt work because our own Validator isnt doing anything in the testcases
    @Disabled
    @Test
    void shouldNotValidateWhenPasswordNotMatch() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.username = "sami02";
        user.firstname = "sam";
        user.lastname = "smith";
        user.password = "12345";
        user.passwordConfirm = "123456";

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        System.out.println(constraintViolations.size());
        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("passwordConfirm");

    }
}
