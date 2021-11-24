package org.springframework.samples.parchisoca.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AuthoritiesServiceTest {

    @Autowired
    AuthoritiesService authService;
    Authorities testAuthority;
    User testUser;

    @BeforeEach
    void initAuth(){
        testUser = new User();
        testUser.setUsername("max");
        testUser.setFirstname("Max");
        testUser.setLastname("Mustermann");
        testUser.setPassword("verysecretpassword");

        testAuthority = new Authorities();
        testAuthority.setUser(testUser);
        testAuthority.setAuthority("testAuth");
        testAuthority.setId(99);

    }

    @Test
    void shouldFindAuthority(){
        authService.saveAuthorities(testAuthority);
        int id = testAuthority.getId();

        Optional<Authorities> optionalAuth = this.authService.findAuth(id);
        assertTrue(optionalAuth.isPresent());
        assertEquals(optionalAuth.get().getUser().username, "max");
    }

    @Disabled
    @Test
    void shouldSaveNewAuthority(){

    }
}
