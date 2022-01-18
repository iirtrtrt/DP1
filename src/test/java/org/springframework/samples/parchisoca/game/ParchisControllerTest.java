package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.web.UserController;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)

@Disabled
public class ParchisControllerTest {

    @Autowired
    private ParchisController parchisController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    private Optional<User> createTestUser(){
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setFirstname("Max");
        testUser.setLastname("Mustermann");
        testUser.setEmail("Max@web.de");
        testUser.setPassword("12345");
        testUser.setPasswordConfirm("12345");
        Optional<User> userOptional = Optional.of(testUser);
        return userOptional;
    }

    @Test
    public void joinGameTest() throws Exception {

        mockMvc.perform(get("/join/{gameid}/quit"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name(":/"));
    }

}
