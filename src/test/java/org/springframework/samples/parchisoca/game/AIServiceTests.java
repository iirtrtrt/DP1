package org.springframework.samples.parchisoca.game;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.game.AI.AIService;
import org.springframework.samples.parchisoca.user.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;



@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    EmailService.class }))
public class AIServiceTests {

   // @MockBean
   // ParchisService parchisService;

    //@Autowired
    //AIService aiService;

    @Test
    public void chooseOneOption(){
/*
        Game game = new Game();
        Parchis parchis = new Parchis();

        Option option = new Option();
        option.setNumber(1);
        parchis.setOptions(new ArrayList<>());

        game.setGameboard(parchis);

        
        game.getGameboard().getOptions().add(option);
*/

       // Mockito.doNothing().when(parchisService).handleState(game);
        //aiService.choosePlay(game, parchisService);

        //assertTrue("Option is not set to true", game.getGameboard().getOptions().get(0).getChoosen());
    }



    
}
