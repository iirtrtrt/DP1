package org.springframework.samples.parchisoca.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.model.user.Authorities;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.model.user.UserRole;
import org.springframework.samples.parchisoca.service.AuthoritiesService;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class AuthoritiesServiceTest {

    @Autowired
    AuthoritiesService authService;
    @Autowired
    UserService userService;

    static Authorities testAuthority;
    static User testUser;


    @Test
    void shouldFindAuthority(){
        Optional<User> optionalUser = userService.findUser("admin");
        User existingUser = optionalUser.get();

        Optional<Authorities> optionalAuth = this.authService.findAuthByUser(existingUser);
        assertTrue(optionalAuth.isPresent());
        assertEquals(optionalAuth.get().getAuthority(), "admin");
    }

    @Test
    void shouldSaveNewAuthority(){
        testUser = new User();
        testUser.setUsername("maxi");
        testUser.setFirstname("Maximilian");
        testUser.setLastname("Mustermann");
        testUser.setPassword("verysecretpassword");
        userService.saveUser(testUser, UserRole.PLAYER);


        testAuthority = new Authorities();
        testAuthority.setUser(testUser);
        testAuthority.setAuthority("testAuth");
        testAuthority.setId(99);

        authService.saveAuthorities(testAuthority);

        Optional<Authorities> optionalAuth = this.authService.findAuthByUser(testUser);
        assertTrue(optionalAuth.isPresent());
        assertEquals(optionalAuth.get().getUser().getUsername(), "maxi");

    }
}
