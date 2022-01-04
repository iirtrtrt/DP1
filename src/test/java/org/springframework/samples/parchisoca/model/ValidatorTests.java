package org.springframework.samples.parchisoca.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Disabled
class ValidatorTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void shouldNotValidateInvalidUserNameAndPassword() {

		User user = new User();
        user.setUsername("hey");
        user.setPassword("hey");


		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

		assertThat(constraintViolations.size()).isEqualTo(2);
    }

    @Test
    void shouldValidateUserNameAndPassword() {

        User user = new User();
        user.setUsername("heyho");
        user.setPassword("heyho");


        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }


}
