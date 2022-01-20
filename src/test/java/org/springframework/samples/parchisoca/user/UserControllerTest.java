package org.springframework.samples.parchisoca.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.service.*;
import org.springframework.samples.parchisoca.web.UserController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthoritiesService authoritiesService;
    @MockBean
    private UserService userService;
    @MockBean
    private GameService gameService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private VerificationTokenService verificationTokenService;

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

    // GET TESTS
    @Test
    public void registerGetTest() throws Exception {
        mockMvc.perform(get("/register"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("users/createOwnerForm"));
    }

    @Test
    public void registerConfirmGetTest() throws Exception {
        mockMvc.perform(get("/register/confirm"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void editProfileGetTest() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        mockMvc.perform(get("/editProfile"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("users/editProfileForm"));
    }

    @Test
    public void statisticsGetTest() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        mockMvc.perform(get("/statistics"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("users/statistics"));
    }

    @Test
    public void adminHomeGetTest() throws Exception {
        mockMvc.perform(get("/admin"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminHome"));
    }

    @Test
    public void adminEditProfileGetTest() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        mockMvc.perform(get("/admin/editProfile"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminEditProfile"));
    }

    @Test
    public void adminUsersGetTest() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        mockMvc.perform(get("/admin/users"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminUsers"));
    }

    @Test
    public void adminGamesGetTest() throws Exception {
        mockMvc.perform(get("/admin/games"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminGames"));


    }

    @Test
    public void adminRegisterGetTest() throws Exception {
        mockMvc.perform(get("/admin/register"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminCreateOwner"));


    }

    @Test
    public void adminUserDetailsGetTest() throws Exception {
        when(userService.getSelectedUser("testUser"))
            .thenReturn(createTestUser().get());
        mockMvc.perform(get("/admin/users/details/{username}", "testUser"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminUsersDetails"));
    }

    @Test
    public void adminDeleteUserTest() throws Exception {
        mockMvc.perform(get("/admin/users/delete/user"))
            .andDo(print())
            .andExpect(status().is(302))
            .andExpect(view().name("redirect:/admin/users"));
    }

    @Test
    public void registerPostTest() throws Exception {
        Optional<User> request = createTestUser();

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8"))
            .andExpect(status().is3xxRedirection())
            .andDo(print());

    }

    @Test
    public void editProfilePost() throws Exception {
        when(userService.findUser("testUser"))
            .thenReturn(createTestUser());
        Optional<User> request = createTestUser();

        mockMvc.perform(post("/editProfile")
                .contentType(MediaType.APPLICATION_JSON)
               // .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/editProfileForm"))
            .andDo(print());
    }

    @Test
    public void adminEditProfilePost() throws Exception {
        when(userService.findUser("testUser"))
            .thenReturn(createTestUser());

        Optional<User> request = createTestUser();

        mockMvc.perform(post("/admin/editProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminEditProfile"))
            .andDo(print());
    }

    @Test
    public void adminUsersPost() throws Exception {
        when(userService.getCurrentUser())
            .thenReturn(createTestUser());
        Optional<User> request = createTestUser();

        mockMvc.perform(post("/admin/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminUsers"))
            .andDo(print());

    }

    @Test
    public void adminRegisterPost() throws Exception {
        Optional<User> request = createTestUser();

        mockMvc.perform(post("/admin/register", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void adminUserDetailsPost() throws Exception {
        Optional<User> request = createTestUser();

        mockMvc.perform(post("/admin/users/details/{username}", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminUsersDetails"))
            .andDo(print());
    }

}
