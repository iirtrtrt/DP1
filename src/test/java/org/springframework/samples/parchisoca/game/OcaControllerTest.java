package org.springframework.samples.parchisoca.game;

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
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.game.GameService;
import org.springframework.samples.parchisoca.user.StatisticUser;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = OcaController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)

public class OcaControllerTest {


        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private OcaController ocaController;

        @Autowired
        private ObjectMapper objectMapper;


        @MockBean
        OcaService ocaService;

        @MockBean
        GameService gameService;

        @MockBean
        UserService userService;


    private Optional<Game> createGame() {
        Game game = new Game();
        User creator = createTestUser().get();
        game.setName("new_game");
        game.setStatus(GameStatus.CREATED);
        game.setAI(false);
       game.setCreator(creator);
        game.setType(GameType.Oca);
        game.setGame_id(1);
        game.setHas_started(false);
        game.setMax_player(2);
        game.setCurrent_players(creator);
        return Optional.of(game);
    }

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

     private String JsonUser(User user) throws Exception{
        System.out.println("user to json" + user);
        Map<String, String> input = new HashMap<>();
        input.put("username", user.getUsername());
        input.put("firstname", user.getFirstname());
        input.put("lastname", user.getLastname());
        input.put("email", user.getEmail());
        input.put("password", user.getPassword());
        input.put("passwordConfirm", user.getPasswordConfirm());
        System.out.println("user to json done" + input);
  
  
        return objectMapper.writeValueAsString(input);
     }



    @Test
    public void redirectToJoinTest() throws Exception{

        when(this.gameService.findGamebyID(1)).thenReturn(createGame());
        mockMvc.perform(get("/game/oca/1"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/game/oca/join/1"));

    }


}
