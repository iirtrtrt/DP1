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
@WebMvcTest(value = ParchisController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)

public class ParchisControllerTest {


        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ParchisController parchisController;

        @Autowired
        private ObjectMapper objectMapper;


        @MockBean
        ParchisService parchisService;

        @MockBean
        GameService gameService;

        @MockBean
        UserService userService;

        @MockBean
        OptionService optionService;


    private Optional<Game> createGame() {
        Game game = new Game();
        User creator = createTestUser().get();
        game.setName("new_game");
        game.setStatus(GameStatus.CREATED);
        game.setAI(false);
       game.setCreator(creator);
        game.setType(GameType.Parchis);
        game.setGame_id(1);
        game.setHas_started(false);
        game.setMax_player(2);
        game.setCurrent_players(creator);
        return Optional.of(game);
    }

    private Optional<Game> finishedGame() {
        Game game = new Game();
        User creator = createTestUser().get();
        game.setName("new_game");
        game.setStatus(GameStatus.FINISHED);
        game.setAI(false);
       game.setCreator(creator);
        game.setType(GameType.Parchis);
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
        Optional<User> userOptional = Optional.of(testUser);
        return userOptional;
     }

     private Optional<Option> createTestChoice(){
        Option testOption = new Option();
        testOption.setId(1);
        testOption.setNumber(2);
        testOption.setText(Option.MOVE);
        Optional<Option> optionOptional = Optional.of(testOption);
        return optionOptional;
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
        mockMvc.perform(get("/game/parchis/1"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/game/parchis/join/1"));

    }

    @Test
public void quitGameTest() throws Exception {

        
        when(this.gameService.findGamebyID(1)).thenReturn(finishedGame());
        mockMvc.perform(get("/game/parchis/join/1/quit"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/"));
    }

    @Test
public void diceGameTest() throws Exception {

        
        when(this.gameService.findGamebyID(1)).thenReturn(createGame());
        
        mockMvc.perform(get("/game/parchis/join/1/dice"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/game/parchis/join/1"));
    }

    @Test
public void choiceGameTest() throws Exception {

        
        when(this.gameService.findGamebyID(1)).thenReturn(createGame());
        when(this.optionService.findOption(1)).thenReturn(createTestChoice());
        
        mockMvc.perform(get("/game/parchis/join/1/choice/1"))
                        .andDo(print())
                        .andExpect(status().isOk());
                        //andExpect(view().name("redirect:/game/parchis/join/1"));
    }



}
