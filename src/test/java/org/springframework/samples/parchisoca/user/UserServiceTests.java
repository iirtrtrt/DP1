package org.springframework.samples.parchisoca.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class UserServiceTests {

    @Autowired
    UserService userService;


    @Test
    void shouldFindExistingUserByUsername()
    {
        Optional<User> optionalUser= this.userService.findUser("flogam1");
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getPassword(), "lolalola");
    }

    @Test
    void shouldNotfindNonexistingUser()
    {
        Optional<User> optionalUser= this.userService.findUser("Idontexist");
        assertFalse(optionalUser.isPresent());
    }

    @Test
    void shouldSaveNewUser()
    {
       User user = new User();
       user.setUsername("max");
       user.setFirstname("Max");
       user.setLastname("Mustermann");
       user.setPassword("verysecretpassword");
       this.userService.saveUser(user);

       Optional<User> optionalUser = this.userService.findUser("max");
       assertTrue(optionalUser.isPresent());
       assertEquals(optionalUser.get().getUsername(), "max");
    }

    @Test
    void findAuthentificationShouldReturnNull()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }


}
