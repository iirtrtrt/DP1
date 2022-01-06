package org.springframework.samples.parchisoca.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.parchisoca.configuration.SecurityConfiguration;
import org.springframework.samples.parchisoca.game.GameService;
import org.springframework.samples.parchisoca.user.*;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
//@WebMvcTest(controllers=UserController.class,
//  excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
//excludeAutoConfiguration= SecurityConfiguration.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = InvitationController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class InvitationControllerTests {


    @Autowired
    private InvitationController invitationController;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private VerificationTokenService verificationTokenService;



   // private StatisticUser mockStatistic = new StatisticUser(1,1,6);
    private Optional<User> createTestUser(){
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setFirstname("Max");
        testUser.setLastname("Mustermann");
        testUser.setEmail("Max@web.de");
        testUser.setPassword("12345");
        testUser.setPasswordConfirm("12345");
        StatisticUser statistic = new StatisticUser(1,1,6);
        testUser.setStatistic(statistic);
        Optional<User> userOptional = Optional.of(testUser);
        return userOptional;
    }
    private Optional<User> createEmailUser(){
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setFirstname("Email");
        testUser.setLastname("User");
        testUser.setEmail("florian.gamillscheg@live.com");
        testUser.setPassword("12345");
        testUser.setPasswordConfirm("12345");
        Optional<User> userOptional = Optional.of(testUser);
        return userOptional;
    }

    @Test
    public void showInvitationForm() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        mockMvc.perform(get("/invite"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("users"))
            .andExpect(view().name("users/invitationForm"));
    }

    @Test
    @Disabled
    public void sendInvitationForm() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        when(userService.findUser("testuser"))
            .thenReturn(createEmailUser());

        mockMvc.perform(post("/invite/{username}", "testuser"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("users/invitationForm"));
    }

}
