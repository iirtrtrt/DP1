
package org.springframework.samples.parchisoca.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.game.AI.AIService;
import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserRole;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.samples.parchisoca.user.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.ActionUtil;


@Service
public class OcaService {


    private static final Logger logger = LogManager.getLogger(OcaService.class);

    @Autowired
    OcaRepository ocaRepository;

    @Autowired
    GameService gameService;

    @Autowired
    BoardFieldRepository boardFieldRepository;

    @Autowired
    BoardFieldService boardFieldService;

    GameRepository gameRepository;

    GameBoardRepository gameBoardRepository;
    @Autowired
    OptionService optionService;

    @Autowired
    AIService aiService;

    @Autowired
    UserService userService;

    public static final String WHITE_COLOR = "#FFFFFF70"; //basic
    public static final String YELLOW_COLOR = "#FFFF0099"; // goose
    public static final String BLUE_COLOR = "#87CEEB"; // bridge
    public static final String ORANGE_COLOR = "#FFA500"; // dice
    public static final String GRAY_COLOR = "#A9A9A9"; // stun
    public static final String BLACK_COLOR = "#000000"; // death
    public static final String GREEN_COLOR = "#90EE90";
    public static final String PURPLE_COLOR = "#CBC3E3";
    public static final String LIGHTBROWN_COLOR = "#C4A484"; // start
    public static final String BROWN_COLOR = "#964B00"; // goal

    public static final Integer FIELD_WIDTH = 1;
    public static final Integer FIELD_HEIGHT = 1;

    public Optional < Oca > findById(Integer id) {
        return ocaRepository.findById(id);
    }

    @Autowired
    public OcaService(OcaRepository ocaRepository, GameRepository gameRepository, GameBoardRepository gameBoardRepository, BoardFieldRepository boardRepo,
        GameService gameService, BoardFieldService boardFieldService, UserService userService, AIService aiservice) {
        this.ocaRepository = ocaRepository;
        this.gameRepository = gameRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.boardFieldRepository = boardRepo;
        this.boardFieldService = boardFieldService;
        this.userService = userService;
        this.gameService = gameService;
        this.boardFieldService = boardFieldService;
        this.userService = userService;
        this.aiService = aiservice;
    }

    public void initGameBoard(Game game) {
        Oca gameBoard = new Oca();
        gameBoard.height = 800;
        gameBoard.width = 800;
        gameBoard.background = "/resources/images/goose6cut2.png";

        //Create Game fields
        logger.info("creating gameFields");

        gameBoard.fields = new ArrayList < BoardField > ();
        // gameBoard.actionFields = new ArrayList <ActionField>();
        this.createGameFields(gameBoard);
        logger.info("finished creating gameFields");

        logger.info("setting gameboard");
        gameBoard.setGame(game);
        game.setGameboard(gameBoard);

        

        try {
            this.gameBoardRepository.save(gameBoard);
        } catch (Exception e) {
            logger.error("ERROR: " + e.getMessage());
        }

        for (BoardField field: gameBoard.getFields()) {
            field.setBoard(gameBoard);
            boardFieldService.saveBoardField(field); 
            
        }

        
        setNextFields2(gameBoard);      

        //set first fieldfor AI as well
        if(game.isAI()){
            for(User player : game.getCurrent_players()){
                if(player.getRole() == UserRole.AI){
                    if(player.getGamePieces().get(0).getField() == null){
                        System.out.println("Setting Startfield");

                        player.getGamePieces().get(0).setField(game.getStartField());
                        System.out.println("Startfield Set");
                       // game.getStartField().getListGamesPiecesPerBoardField().add(player.getGamePieces().get(0));
                        userService.saveUser(player, UserRole.AI);
                    }
                }
            }
        }
    }

    

    public void handleState(Game game) {
        switch (game.getTurn_state()) {
            case INIT:
                logger.info("Handle State Init, " + game.getCurrent_player().getFirstname());
                StateInitOca.doAction(game);
                break;
            case ROLLDICE:
                logger.info("Handle State ROLLDICE, " + game.getCurrent_player().getFirstname());
                StateRollDiceOca.doAction(game);
                break;
            case DIRECTPASS:
                logger.info("Handle State DIRECTPASS, " + game.getCurrent_player().getFirstname());
                StateDirectPassOca.doAction(game);
                if(game.getCurrent_player().getRole() == UserRole.AI){
                    logger.info("AI chooses play");
                    aiService.choosePlay(game, this);
                }
            break;
            case CHOOSEPLAY:
                logger.info("Handle State CHOOSEPLAY, " + game.getCurrent_player().getFirstname());
                StateChoosePlayOca.doAction(game);
                if(game.getCurrent_player().getRole() == UserRole.AI){
                    logger.info("AI chooses play");
                    aiService.choosePlay(game, this);
                }
                break;
            case PASSMOVE:
                logger.info("Handle State PASSMOVE, " + game.getCurrent_player().getFirstname());
                StatePassMoveOca.doAction(game);
                break;
            case MOVE:
                logger.info("Handle State MOVE, " + game.getCurrent_player().getFirstname());
                StateMoveOca.doAction(game);
                break;
            

            case NEXT:
                logger.info("Handle State NEXT, " + game.getCurrent_player().getFirstname());
                if(game.getTurns().size()<game.getMax_player()){
                    StateNextOca.doActionI(game);}
                else{
                    StateNextOca.doActionI(game);
                }
                break;
            case FINISHED:
            StateFinished.doAction(game);
            break;
            }    
            
            logger.info("current state: " + game.getTurn_state()); 
    }


    
    public void setNextFields2(GameBoard board){
        for (BoardField field: board.getFields()) {

        
            BoardField next = null;
            if (field.getNumber() != 63) {
                next = boardFieldService.find(field.getNumber() + 1, board);

                if(field.getAction() != null){
                    if(field.getAction().equals(ActionType.DICE) && field.getNumber()==26){ next = boardFieldService.find(53, board);}
                    else if(field.getAction().equals(ActionType.DEATH)){next = boardFieldService.find(0, board);}
                    else if(field.getAction().equals(ActionType.DICE) && field.getNumber()==53){ next = boardFieldService.find(26, board);}
                    else if(field.getAction().equals(ActionType.BRIDGE) && field.getNumber()==6) { next = boardFieldService.find(12, board);}
                    else if(field.getAction().equals(ActionType.BRIDGE) && field.getNumber()==12) { next = boardFieldService.find(6, board);} 
                    else if(field.getAction().equals(ActionType.GOOSE)){next =  nextGoose(field, board);}
                }    
            }
            field.setNext_field(next);    
            boardFieldService.saveBoardField(field); 
        }
    }
    


    //Calculates all the Board Field entities that are needed
    public void createGameFields(Oca gameboard) {
        int id;
        int column;
        int row;
        BoardField start_field = null;     

        BoardField aField = null;

        //ids 0 to 7
        id = 0;
        row = 7;
        for (column = 0; column <= 7; column++) {
            if (id == 0) {
                start_field = new BoardField(id, LIGHTBROWN_COLOR, FieldType.START, column, row, FIELD_WIDTH, FIELD_HEIGHT);
                gameboard.fields.add(start_field);
            } else if (id == 5 || id == 1) {
                gameboard.fields.add(new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE));
                
            } else if (id == 6) {
                aField = new BoardField(id, BLUE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.BRIDGE);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 14 to 8
        column = 7;
        id = 14;
        for (row = 0; row <= 6; row++) {
            if (id == 9 || id == 14) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);

            } else if (id == 12) {
                aField = new BoardField(id, BLUE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.BRIDGE);
                gameboard.fields.add(aField);

            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 21 to 15
        id = 21;
        row = 0;
        for (column = 0; column <= 6; column++) {
            if (id == 18) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);
            } else if (id == 19) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.INN);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }


        //ids 22 to 27
        column = 0;
        id = 22;
        for (row = 1; row <= 6; row++) {
            if (id == 23 || id == 27) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);
            } else if (id == 26) {
                aField = new BoardField(id, ORANGE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.DICE);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 28 to 33
        row = 6;
        id = 28;
        for (column = 1; column <= 6; column++) {
            if (id == 31) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.HORIZONTAL, column, row, FIELD_HEIGHT, FIELD_WIDTH, ActionType.WELL);
                gameboard.fields.add(aField);
            } else if (id == 32) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            }
            id++;
        }

        //ids 38 to 34
        column = 6;
        id = 38;
        for (row = 1; row <= 5; row++) {
            if (id == 36) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);

            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 43 to 39
        id = 43;
        row = 1;
        for (column = 1; column <= 5; column++) {
            if (id == 41) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);
            } else if (id == 42) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.MAZE);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 44 to 47
        id = 44;
        column = 1;
        for (row = 2; row <= 5; row++) {
            if (id == 45) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 48 to 51
        id = 48;
        row = 5;
        for (column = 2; column <= 5; column++) {
            if (id == 50) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 54 to 52
        id = 54;
        column = 5;
        for (row = 2; row <= 4; row++) {
            if (id == 52) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.JAIL);
                gameboard.fields.add(aField);
            } else if (id == 53) {
                aField = new BoardField(id, ORANGE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.DICE);
                gameboard.fields.add(aField);
            } else if (id == 54) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);

            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 57 to 55
        id = 57;
        row = 2;
        for (column = 2; column <= 4; column++) {
            gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id--;
        }
        //ids 58 to 59
        id = 58;
        column = 2;
        for (row = 3; row <= 4; row++) {
            if (id == 58) {
                aField = new BoardField(id, BLACK_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.DEATH);
                gameboard.fields.add(aField);
            } else if (id == 59) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.fields.add(aField);
            } else {
                gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 60 to 61
        id = 60;
        row = 4;
        for (column = 3; column <= 4; column++) {
            gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id++;
        }

        //id 62
        id = 62;
        column = 4;
        row = 3;
        gameboard.fields.add(new BoardField(id, WHITE_COLOR, FieldType.SQUARE, column, row, FIELD_WIDTH, FIELD_HEIGHT));

        //id 63
        id = 63;
        column = 3;
        row = 3;
        gameboard.fields.add(new BoardField(id, BROWN_COLOR, FieldType.END, column, row, FIELD_WIDTH, FIELD_HEIGHT));

    }

    private BoardField nextGoose(BoardField actualGoose, GameBoard board){
        Integer nextGooseNumber = 1;
        List<BoardField> fields = board.getFields();
        List<Integer> goosPos = new ArrayList<Integer>();
        Map<BoardField, Integer> nextPositions = new HashMap<BoardField, Integer>(); 
        for(BoardField field : fields){
            if(field.getAction() != null){
                if(field.getAction().equals(ActionType.GOOSE)){
                    goosPos.add(field.getNumber());
                }
            }
        }
        Collections.sort(goosPos);

        List<BoardField> sortedGooses = new ArrayList<BoardField>();

        for(Integer i : goosPos){   
            sortedGooses.add(boardFieldService.find(i, board));
        }

        goosPos.add(63);
        goosPos.remove(0);

        for(int i = 0; i<sortedGooses.size(); i++){
            nextPositions.put(sortedGooses.get(i), goosPos.get(i));
        }   
        nextGooseNumber = nextPositions.get(actualGoose);

        

        return boardFieldService.find(nextGooseNumber, board);
    }


    @Transactional
    public void saveOca(Oca oca) throws DataAccessException {
        ocaRepository.save(oca);
    }

    

    
    
}
