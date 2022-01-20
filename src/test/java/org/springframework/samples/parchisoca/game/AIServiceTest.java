package org.springframework.samples.parchisoca.game;

import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.model.game.AI.AIService;
import org.springframework.samples.parchisoca.model.game.AI.AStrategy_End;
import org.springframework.samples.parchisoca.model.game.AI.BStrategy_KickPlayer;
import org.springframework.samples.parchisoca.model.game.AI.CStrategy_EndZone;
import org.springframework.samples.parchisoca.model.game.AI.DStrategy_SaveField;
import org.springframework.samples.parchisoca.model.game.AI.EStrategy_MoveFrontPlayer;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.model.user.UserRole;
import org.springframework.samples.parchisoca.model.game.Parchis;
import org.springframework.samples.parchisoca.model.game.BoardField;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.GamePiece;
import org.springframework.samples.parchisoca.model.game.Oca;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.service.ParchisService;
import org.springframework.samples.parchisoca.service.BoardFieldService;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.samples.parchisoca.service.OcaService;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class AIServiceTest {

   @MockBean
   ParchisService parchisService;
   @MockBean
   OcaService ocaService;

   @Autowired
   AIService aiService;

   @Autowired
   AStrategy_End end;

   @Autowired
   BStrategy_KickPlayer kick;

   @Autowired
   CStrategy_EndZone endzone;

   @Autowired
   DStrategy_SaveField save;

   @Autowired
   EStrategy_MoveFrontPlayer movefront;

   @MockBean
   OptionService optionService;

   @MockBean
   BoardFieldService boardFieldService;

   private static final Map < Color, Integer> color_endzone = Map.of(
    Color.RED,  34,
    Color.BLUE, 17,
    Color.GREEN, 51,
    Color.YELLOW, 68
);
   

    private User RedUser(){

       User user = new User();
       user.setUsername("username");
       user.setPassword("password");
       user.setTokenColor(Color.RED);
       user.setRole(UserRole.AI);
       return user;

    }

    private BoardField newField(){
        BoardField field = new BoardField();
        field.setListGamesPiecesPerBoardField(new ArrayList<>());
        return field;

    }



   @Test
   public void chooseOneOptionParchis(){

       Game game = new Game();
       game.setName("Game");
       Parchis parchis = new Parchis();
       Option option = new Option();
       option.setNumber(1);
       parchis.setOptions(new ArrayList<>());
       game.setGameboard(parchis);

       game.getGameboard().getOptions().add(option);
       Mockito.doNothing().when(parchisService).handleState(game);
       aiService.choosePlay(game, parchisService);
       assertTrue("Option is not set to true", game.getGameboard().getOptions().get(0).getChoosen());
   }

   @Test
   public void chooseOneOptionOca(){

       Game game = new Game();
       game.setName("Game");
       Oca oca = new Oca();
       Option option = new Option();
       option.setNumber(1);
       oca.setOptions(new ArrayList<>());
       game.setGameboard(oca);

       game.getGameboard().getOptions().add(option);
       Mockito.doNothing().when(ocaService).handleState(game);
       aiService.choosePlay(game, ocaService);
       assertTrue("Option is not set to true", game.getGameboard().getOptions().get(0).getChoosen());
   }


    @ParameterizedTest
    @CsvSource({"44,58", "40,2", "68, 34"})
   public void chooseFrontPlayer(String nr1, String nr2){
       
       Game game = new Game();
       game.setDice(1);
       game.setName("Game");
       Parchis parchis = new Parchis();
       Option option1 = new Option();
       option1.setNumber(1);
       option1.setText(Option.MOVE + nr1);
       Option option2 = new Option();
       option2.setNumber(2);
       option2.setText(Option.MOVE + nr2);


       User user = RedUser();
       game.setCurrent_player(user);

       parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
       game.setGameboard(parchis);

       movefront.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
 
       assertFalse("Wrong Option is set to true" + game.getGameboard().getOptions().get(0).getText(), game.getGameboard().getOptions().get(0).getChoosen());
       assertTrue("Option is not set to true" + game.getGameboard().getOptions().get(1).getText(), game.getGameboard().getOptions().get(1).getChoosen());
        
    }

    @Test 
    public void kickPlayerNO(){

        Game game = new Game();
        game.setDice(3);
        game.setName("Game");
        Parchis parchis = new Parchis();
        Option option1 = new Option();
        option1.setNumber(1);
        option1.setText(Option.MOVE + "9");
        Option option2 = new Option();
        option2.setNumber(2);
        option2.setText(Option.MOVE + "10");
        

        User user = RedUser();
 
        game.setCurrent_player(user);
 
        parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
        game.setGameboard(parchis);

        when(boardFieldService.find(9 + 3, game.getGameboard()))
            .thenReturn(newField());

        when(boardFieldService.find(10 + 3, game.getGameboard()))
            .thenReturn(newField());

        boolean found = kick.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
        assertFalse("option found but should not have been", found);

    }

    @Test 
    public void kickPlayerSpecial(){

        Game game = new Game();
        game.setDice(3);
        game.setName("Game");
        Parchis parchis = new Parchis();
        Option option1 = new Option();
        option1.setNumber(1);
        option1.setText(Option.MOVE + "9");
        Option option2 = new Option();
        option2.setNumber(2);
        option2.setText(Option.MOVE + "10");
        

        User user = RedUser();
        User user2 = RedUser();
        user2.setRole(UserRole.PLAYER);
        game.setCurrent_player(user);
 
        parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
        game.setGameboard(parchis);

        BoardField field_with_player = newField();

        GamePiece piece = new GamePiece();
        piece.setField(field_with_player);
        piece.setUser_id(user2);
        field_with_player.getListGamesPiecesPerBoardField().add(piece);
        field_with_player.setParchis_special(true);
        

        when(boardFieldService.find(9 + 3, game.getGameboard()))
            .thenReturn(newField());

        when(boardFieldService.find(10 + 3, game.getGameboard()))
            .thenReturn(field_with_player);

        boolean found = kick.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
        assertFalse("option kick player found but should not have been", found);

    }

    @Test 
    public void kickPlayerYes(){

        Game game = new Game();
        game.setDice(3);
        game.setName("Game");
        Parchis parchis = new Parchis();
        Option option1 = new Option();
        option1.setNumber(1);
        option1.setText(Option.MOVE + "9");
        Option option2 = new Option();
        option2.setNumber(2);
        option2.setText(Option.MOVE + "10");
        

        User user = RedUser();
        User user2 = RedUser();
        user2.setRole(UserRole.PLAYER);

        game.setCurrent_player(user);
 
        parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
        game.setGameboard(parchis);

        BoardField field_with_player = newField();

        GamePiece piece = new GamePiece();
        piece.setField(field_with_player);
        piece.setUser_id(user2);
        field_with_player.getListGamesPiecesPerBoardField().add(piece);
        

        when(boardFieldService.find(9 + 3, game.getGameboard()))
            .thenReturn(newField());

        when(boardFieldService.find(10 + 3, game.getGameboard()))
            .thenReturn(field_with_player);

        boolean found = kick.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
        assertTrue("option not found but should have been", found);
        assertTrue("Option is not set to true: " + game.getGameboard().getOptions().get(0).getText(), game.getGameboard().getOptions().get(1).getChoosen());

    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 4, 6})
    public void CheckIfEndYes(int dice){

        Game game = new Game();
        game.setDice(dice);
        game.setName("Game");
        Parchis parchis = new Parchis();
        Option option1 = new Option();
        option1.setNumber(1);
        option1.setText(Option.MOVE + (141 - dice));
        Option option2 = new Option();
        option2.setNumber(2);
        option2.setText(Option.MOVE + "10");
        
        game.setCurrent_player(RedUser());
 
        parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
        game.setGameboard(parchis);

        boolean found = end.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
        assertTrue("option not found but should have been", found);
        assertTrue("Option is not set to true: " + game.getGameboard().getOptions().get(0).getText(), game.getGameboard().getOptions().get(0).getChoosen());

    }

    @Test
    public void CheckIfEndNo(){

        Game game = new Game();
        game.setDice(1);
        game.setName("Game");
        Parchis parchis = new Parchis();
        Option option1 = new Option();
        option1.setNumber(1);
        option1.setText(Option.MOVE + (139));
        Option option2 = new Option();
        option2.setNumber(2);
        option2.setText(Option.MOVE + "10");
        
        game.setCurrent_player(RedUser());
 
        parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
        game.setGameboard(parchis);

        boolean found = end.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
        assertFalse("option not found but should have been", found);

    }

    @Test
    public void CheckIfSaveFieldYes(){

        Game game = new Game();
        game.setDice(3);
        game.setName("Game");
        Parchis parchis = new Parchis();
        Option option1 = new Option();
        option1.setNumber(1);
        option1.setText(Option.MOVE + "9");
        Option option2 = new Option();
        option2.setNumber(2);
        option2.setText(Option.MOVE + "10");
        
        game.setCurrent_player(RedUser());
 
        parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
        game.setGameboard(parchis);

        BoardField save_field = newField();
        save_field.setParchis_special(true);

        when(boardFieldService.find(9 + 3, game.getGameboard()))
            .thenReturn(newField());

        when(boardFieldService.find(10 + 3, game.getGameboard()))
            .thenReturn(save_field);

        boolean found = save.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
        assertTrue("option not found but should have been", found);
        assertTrue("Option is not set to true: " + game.getGameboard().getOptions().get(0).getText(), game.getGameboard().getOptions().get(1).getChoosen());

    }

    @Test
    public void CheckIfSaveFieldNo(){

        Game game = new Game();
        game.setDice(3);
        game.setName("Game");
        Parchis parchis = new Parchis();
        Option option1 = new Option();
        option1.setNumber(1);
        option1.setText(Option.MOVE + "9");
        Option option2 = new Option();
        option2.setNumber(2);
        option2.setText(Option.MOVE + "10");
        
        game.setCurrent_player(RedUser());
 
        parchis.setOptions(new ArrayList<>(Arrays.asList(option1, option2)));
        game.setGameboard(parchis);

        when(boardFieldService.find(9 + 3, game.getGameboard()))
            .thenReturn(newField());

        when(boardFieldService.find(10 + 3, game.getGameboard()))
            .thenReturn(newField());

        boolean found = save.checkStrategy(game.getGameboard().getOptions(), game, boardFieldService, optionService);
        assertFalse("option found but should not have been", found);
    }




}
