package org.springframework.samples.parchisoca.game;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.model.game.AI.AIService;
import org.springframework.samples.parchisoca.model.game.Parchis;
import org.springframework.samples.parchisoca.model.game.Game;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.service.ParchisService;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))

public class AIServiceTest {

   @MockBean
   ParchisService parchisService;

   @Autowired
   AIService aiService;

   @Test
   public void chooseOneOption(){

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
   public void chooseFrontPlayer(){
       /*
       Game game = new Game();
       game.setDice(1);
       game.setName("Game");
       Parchis parchis = new Parchis();
       Option option = new Option();
       option.setNumber(1);
       option.setText(Option.MOVE + "9");
       Option option2 = new Option();
       option.setNumber(2);
       option.setText(Option.MOVE + "10");

       parchis.setOptions(new ArrayList<>());
       game.setGameboard(parchis);

       game.getGameboard().getOptions().add(option);
       game.getGameboard().getOptions().add(option2);

       Mockito.doNothing().when(parchisService).handleState(game);

       aiService.choosePlay(game, parchisService);
       assertFalse("Wrong Option is set to true" + game.getGameboard().getOptions().get(0).getText(), game.getGameboard().getOptions().get(0).getChoosen());
       assertTrue("Option is not set to true" + game.getGameboard().getOptions().get(1).getText(), game.getGameboard().getOptions().get(1).getChoosen());
        */
    }


}
