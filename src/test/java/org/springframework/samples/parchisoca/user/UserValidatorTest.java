package org.springframework.samples.parchisoca.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.model.user.UserValidator;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class UserValidatorTest {

    private final UserValidator validator = new UserValidator();


    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldNotValidateWhenUsernameEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.setUsername("");
        user.setFirstname("sam");
        user.setLastname("smith");
        user.setEmail("t@web.de");
        user.setPassword("12345");
        user.setPasswordConfirm("12345");

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
    }

    @Test
    void validateUserHasErrors()
    {
        User user = new User();
        user.setUsername("tesuser");
        user.setEmail("t@web.de");
        user.setPassword("verysecretpassword");
        user.setPasswordConfirm("verysecretpasswordwhichisnotthesame");

        // when
        Errors errors = new BeanPropertyBindingResult(user, "");

        validator.validate(user, errors);
        assertTrue(errors.getFieldErrorCount() != 0);
        assertTrue(errors.getFieldErrors("passwordConfirm").size() == 1);
    }

    @Test
    void validateTooShortPassword()
    {
        User user = new User();
        user.setUsername("tesuser");
        user.setEmail("t@web.de");
        user.setPassword("hey");
        user.setPasswordConfirm("hey");

        Errors errors = new BeanPropertyBindingResult(user, "");

        validator.validate(user, errors);
        assertThat(errors.getFieldError("password").getCodes()[0].equals("passwordshort"));

    }

    @Test
    void shouldNotValidateWhenUsernameTooShort() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.setUsername( "s");
        user.setFirstname("sam");
        user.setLastname("smith");
        user.setEmail("t@web.de");
        user.setPassword("12345");
        user.setPasswordConfirm("12345");

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
    }

    @Test
    void shouldNotValidateWhenPasswordEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.setUsername( "sami02");
        user.setFirstname("sam");
        user.setLastname("smith");
        user.setEmail("t@web.de");
        user.setPassword("");
        user.setPasswordConfirm( "");

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
    }

    @Test
    void shouldNotValidateWhenPasswordTooShort() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();
        user.setUsername("sami02");
        user.setFirstname("sam");
        user.setLastname("smith");
        user.setEmail("t@web.de");
        user.setPassword("123");
        user.setPasswordConfirm("123");

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        ConstraintViolation<User> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
    }

    @Test
    void shouldNotValidateWhenEmailIsNotValid() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("tweb.de");
        user.setPassword("hey1");
        user.setPasswordConfirm("hey1");

        Errors errors = new BeanPropertyBindingResult(user, "");

        validator.validate(user, errors);
        assertThat(errors.getFieldError("email").getCodes()[0].equals("emailInvalid"));
    }

}
