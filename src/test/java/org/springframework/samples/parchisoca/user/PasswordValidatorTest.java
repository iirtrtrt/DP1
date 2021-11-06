package org.springframework.samples.parchisoca.user;

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
public class PasswordValidatorTest {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
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

        System.out.println(constraintViolations.size());
        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
    }

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

        //assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("passwordConfirm");
        assertThat(violation.getMessage()).isEqualTo("password does not match");
    }

    @Test
    void shouldNotValidateWhenPasswordToShort() {

    }

    @Test
    void shouldNotValidateWhenPasswordNotMatch() {

    }
}
