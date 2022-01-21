package org.springframework.samples.parchisoca.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.samples.parchisoca.service.UserService;
import org.springframework.samples.parchisoca.service.VerificationTokenService;
import org.springframework.samples.parchisoca.web.InvitationController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(controllers=UserController.class,
//  excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
//excludeAutoConfiguration= SecurityConfiguration.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = InvitationController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class InvitationControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private VerificationTokenService verificationTokenService;


 
    // private StatisticUser mockStatistic = new StatisticUser(1,1,6);
    private Optional<User> createTestUser() {
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
    public void showInvitationForm() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        mockMvc.perform(get("/invite"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("users/invitationForm"));
    }



}
