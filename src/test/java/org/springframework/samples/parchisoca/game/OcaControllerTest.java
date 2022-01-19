package org.springframework.samples.parchisoca.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.Assert.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
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

        @MockBean
        OptionService optionService;


    private Optional<Game> createGame() {
        Game game = new Game();
        game.setGame_id(1);
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

    private Optional<Game> finishedGame() {
        Game game = new Game();
        User creator = createTestUser().get();
        game.setName("new_game");
        game.setStatus(GameStatus.FINISHED);
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
        GamePiece gamePiece = new GamePiece();
        gamePiece.setField(new BoardField());
        testUser.setGamePieces(List.of(gamePiece));
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
        testOption.setText(Option.MOVE_OCA);
        Optional<Option> optionOptional = Optional.of(testOption);
        return optionOptional;
     }


    @Test
    public void redirectToJoinTest() throws Exception{

        when(this.gameService.findGamebyID(1)).thenReturn(createGame());
        mockMvc.perform(get("/game/oca/1"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/game/oca/join/1"));

    }


    @Test
    public void joinOca() throws Exception{

        when(gameService.findGamebyID(1)).thenReturn(createGame());
        when(gameService.findGamebyID(1)).thenReturn(createGame());
        when(userService.getCurrentUser()).thenReturn(createTestUser());

        mockMvc.perform(get("/game/oca/join/{gameid}", 1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("game/ocaGame"));
    }

    @Test
    public void joinOcaTestShouldThrowException() throws Exception{

        when(gameService.findGamebyID(1)).thenReturn(createGame());
        mockMvc.perform(get("/game/oca/join/{gameid}", 1))
            .andDo(print())
           .andExpect(status().isOk())
            .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NoSuchElementException));
    }

    @Test
public void quitGameTest() throws Exception {


        when(this.gameService.findGamebyID(1)).thenReturn(finishedGame());
        mockMvc.perform(get("/game/oca/join/1/quit"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/"));
    }

    @Test
public void diceGameTest() throws Exception {


        when(this.gameService.findGamebyID(1)).thenReturn(createGame());

        mockMvc.perform(get("/game/oca/join/1/dice"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/game/oca/join/1"));
    }

    @Test
    
public void choiceGameTest() throws Exception {

        when(this.gameService.findGamebyID(1)).thenReturn(createGame());
        when(this.optionService.findOption(1)).thenReturn(createTestChoice());

        mockMvc.perform(get("/game/oca/join/1/choice/1"))
                        .andDo(print())
                        .andExpect(status().isOk());
                        //.andExpect(view().name("redirect:/game/oca/join/1"));
    }


}
