package org.springframework.samples.parchisoca.game;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OcaController {

    @Autowired
    OcaService ocaService;

    @Autowired
    GameService gameService;

    private static final String VIEWS_GAME = "game/ocaGame";

    @Autowired
    public OcaController(GameService gameService, OcaService ocaService){
        this.ocaService = ocaService;
        this.gameService = gameService;
    }


    @GetMapping(value = "/game/oca/{gameid}")
    public String initCanvasForm(@PathVariable("gameid") int gameid, ModelMap model, HttpServletResponse response) {
        Game game = this.gameService.findGamebyID(gameid).get();

        ocaService.initGameBoard(game);

        System.out.println("game width:  " + game.getGameboard().getWidth());
        System.out.println("game height:  " + game.getGameboard().getHeight());

        model.put("game",game);
        return VIEWS_GAME;

        // Oca ocaGame = new Oca();

        // try {
        // BufferedImage image = //CALL_OR_CREATE_YOUR_IMAGE_OBJECT;
        // ImageIO.write(image, "jpg", ocaGame);
        // } catch (IllegalArgumentException e) {
        // response.sendError(HttpServletResponse.SC_NOT_FOUND);
        // }

        // byte[] imgByte = ocaGame.toByteArray();

        // response.setHeader("Cache-Control", "no-store");
        // response.setHeader("Pragma", "no-cache");
        // response.setDateHeader("Expires", 0);
        // response.setContentType("/resources/images/board_oca.jpg");
        // ServletOutputStream responseOutputStream = response.getOutputStream();
        // responseOutputStream.write(imgByte);
        // responseOutputStream.flush();
        // responseOutputStream.close();

        // //response.addHeader("Refresh","1"); 
        // System.out.println("before find id");
        // //Todo should not be hard coded
        // Oca ocaGame = new Oca();
        // ocaGame.background = "/resources/static/resources/images/board_oca.jpg";
        // ocaGame.height = 800;
        // ocaGame.width = 800;
        // ocaGame.fields = new ArrayList<BoardField>();
        // ocaService.createGameFields(ocaGame.fields);
        // model.put("oca",ocaGame);
    
    }
}
