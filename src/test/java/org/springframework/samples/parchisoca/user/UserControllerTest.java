package org.springframework.samples.parchisoca.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.parchisoca.game.GameService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserControllerTest {
    @Autowired
    private UserController userController;


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

    private Optional<User> createTestUser(){
        User testUser = new User();
        testUser.username = "testuser";
        testUser.firstname = "Max";
        testUser.lastname = "Mustermann";
        testUser.email = "Max@web.de";
        testUser.password = "12345";
        testUser.passwordConfirm = "12345";
        StatisticUser statistic = new StatisticUser(1,1,6);
        testUser.setStatistic(statistic);
        Optional<User> userOptional = Optional.of(testUser);
        return userOptional;
    }



    //GET TESTS
    @Test
    public void registerPageTest() throws Exception {
        mockMvc.perform(get("/register"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("users/createOwnerForm"));
    }

    @Test
    public void registerConfirmTest() throws Exception {
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

    //exceptions, not working right now
    @Disabled
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
    public void adminHomepageGetTest() throws Exception {
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
    public void adminRegisterPageTest() throws Exception {
        mockMvc.perform(get("/admin/register"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminCreateOwner"));
    }

    @Test
    public void adminUserDetailsPageTest() throws Exception {
        when(userService.getSelectedUser("testUser"))
            .thenReturn(createTestUser().get());
        mockMvc.perform(get("/admin/users/details/{username}", "testUser"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminUsersDetails"));
    }

    //Not working. Problems with redirect. Error 302
    @Disabled
    @Test
    public void adminDeleteUserPageTest() throws Exception {
        mockMvc.perform(get("/admin/users/delete/{username}", "testUser"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admins/adminUsersDetails"));
    }





    //POST TESTS
    //not working right now
    @Disabled
    @Test
    public void registerPost() throws Exception {
//        Map<String, String> request = new HashMap<>();
//        request.put("username", "test");
//        request.put("email", "test@google.com");
//        request.put("password", "1111");
//        request.put("passwordConfirm", "1111");
        Optional <User> request = createTestUser();

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/createOwnerForm"))
            .andDo(print());
    }

    @Disabled
    @Test
    public void editProfilePost(){

    }

    @Disabled
    @Test
    public void adminEditProfilePost(){

    }

    @Disabled
    @Test
    public void adminUsersPost(){

    }

    // is this Post even used in our game?
    @Disabled
    @Test
    public void adminGamesPost(){

    }

    @Disabled
    @Test
    public void adminRegisterPost(){

    }

    @Disabled
    @Test
    public void adminUserDetailsPost(){

    }




}
