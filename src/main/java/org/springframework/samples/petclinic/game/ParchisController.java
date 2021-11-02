package org.springframework.samples.petclinic.game;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ParchisController {

    @Autowired
    ParchisService parchisService;

    private static final String VIEWS_GAME = "game/newgame";


    @GetMapping(value = "/parchis")
    public String initCanvasForm( ModelMap model, HttpServletResponse response) {
        //response.addHeader("Refresh","1"); 
        System.out.println("before find id");
        //Todo should not be hard coded
        Parchis new_game = new Parchis();
        new_game.background = "resources/images/background_board.jpg";
        new_game.height = 800;
        new_game.width = 800;
        new_game.fields = new ArrayList<BoardField>();
        parchisService.createGameFields(new_game.fields);
        model.put("parchis",new_game);
        return VIEWS_GAME;
    }
    
}
