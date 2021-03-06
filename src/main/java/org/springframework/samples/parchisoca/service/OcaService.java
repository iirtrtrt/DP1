
package org.springframework.samples.parchisoca.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.model.game.AI.AIService;
import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.model.game.Oca;
import org.springframework.samples.parchisoca.model.game.*;
import org.springframework.samples.parchisoca.repository.GameBoardRepository;
import org.springframework.samples.parchisoca.repository.OcaRepository;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.model.user.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OcaService {


    private static final Logger logger = LogManager.getLogger(OcaService.class);

    @Autowired
    private OcaRepository ocaRepository;


    @Autowired
    private BoardFieldService boardFieldService;


    private GameBoardRepository gameBoardRepository;

    @Autowired
    private AIService aiService;

    @Autowired
    private UserService userService;

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

    public static final Integer GOAL_FIELD = 63;
    private static final Integer DICE_FIELD_1 = 26;
    private static final Integer DICE_FIELD_2 = 53;
    private static final Integer BRIDGE_FIELD_1 = 6;
    private static final Integer BRIDGE_FIELD_2 = 12;
    private static final Integer INITIAL_FIELD = 0;






    @Autowired
    public OcaService(OcaRepository ocaRepository, GameBoardRepository gameBoardRepository,
                       BoardFieldService boardFieldService, UserService userService, AIService aiservice) {
        this.ocaRepository = ocaRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.boardFieldService = boardFieldService;
        this.userService = userService;
        this.aiService = aiservice;
    }

    public void initGameBoard(Game game) {
        Oca gameBoard = new Oca();
        gameBoard.setHeight(800);
        gameBoard.setWidth(800);
        gameBoard.setBackground("/resources/images/goose6cut2.png");

        //Create Game fields
        logger.info("creating gameFields");

        gameBoard.setFields(new ArrayList < BoardField > ());
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
        setNextFields(gameBoard);

        //set first fieldfor AI as well
        if(game.isAI()){
            for(User player : game.getCurrent_players()){
                if(player.getRole() == UserRole.AI){
                    if(player.getGamePieces().get(0).getField() == null){

                        player.getGamePieces().get(0).setField(game.getStartField());
                        userService.saveUser(player, UserRole.AI);
                    }
                }
            }
        }
    }


    public Optional < Oca > findById(Integer id) {
        return ocaRepository.findById(id);
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
                User myuser = userService.getCurrentUser().get();
                myuser.setRolledDices(myuser.getRolledDices() + 1);
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



    public void setNextFields(GameBoard board){
        for (BoardField field: board.getFields()) {


            BoardField next = null;
            if (field.getNumber() != GOAL_FIELD) {
                next = boardFieldService.find(field.getNumber() + 1, board);

                if(field.getAction() != null){
                    if(field.getAction().equals(ActionType.DICE) && field.getNumber()==DICE_FIELD_1){ next = boardFieldService.find(DICE_FIELD_2, board);}
                    else if(field.getAction().equals(ActionType.DEATH)){next = boardFieldService.find(INITIAL_FIELD, board);}
                    else if(field.getAction().equals(ActionType.DICE) && field.getNumber()==DICE_FIELD_2){ next = boardFieldService.find(DICE_FIELD_1, board);}
                    else if(field.getAction().equals(ActionType.BRIDGE) && field.getNumber()==BRIDGE_FIELD_2) { next = boardFieldService.find(BRIDGE_FIELD_1, board);}
                    else if(field.getAction().equals(ActionType.BRIDGE) && field.getNumber()==BRIDGE_FIELD_1) { next = boardFieldService.find(BRIDGE_FIELD_2, board);}
                    else if(field.getAction().equals(ActionType.GOOSE)){next =  nextGoose(field, board);}
                }
            }
            field.setNext_field(next);
            boardFieldService.saveBoardField(field);
        }
    }



    //Calculates all the Board Field entities that are needed

    private void createStartField(Oca gameboard, int id, int row)
    {
        BoardField start_field = null;
        for (int column = 0; column <= 7; column++) {
            if (id == 0) {
                start_field = new BoardField(id, LIGHTBROWN_COLOR, FieldType.START, column, row, FIELD_WIDTH, FIELD_HEIGHT);
                gameboard.getFields().add(start_field);
            } else if (id == 5 || id == 1) {
                gameboard.getFields().add(new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE));

            } else if (id == 6) {
                BoardField aField = new BoardField(id, BLUE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.BRIDGE);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }
    }


    private void createSimpleField(Oca gameboard, int id, int column, int row, String field_color, FieldType fieldType)
    {
        //id 62
        gameboard.getFields().add(new BoardField(id, field_color, fieldType, column, row, FIELD_WIDTH, FIELD_HEIGHT));

    }
        public void createGameFields(Oca gameboard) {
        int id;
        int column;
        int row;

        BoardField aField = null;

        //ids 0 to 7
        createStartField(gameboard, 0, 7);

        //ids 14 to 8
        column = 7;
        id = 14;
        for (row = 0; row <= 6; row++) {
            if (id == 9 || id == 14) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);

            } else if (id == 12) {
                aField = new BoardField(id, BLUE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.BRIDGE);
                gameboard.getFields().add(aField);

            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 21 to 15
        id = 21;
        row = 0;
        for (column = 0; column <= 6; column++) {
            if (id == 18) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);
            } else if (id == 19) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.INN);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }


        //ids 22 to 27
        column = 0;
        id = 22;
        for (row = 1; row <= 6; row++) {
            if (id == 23 || id == 27) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);
            } else if (id == 26) {
                aField = new BoardField(id, ORANGE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.DICE);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 28 to 33
        row = 6;
        id = 28;
        for (column = 1; column <= 6; column++) {
            if (id == 31) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.HORIZONTAL, column, row, FIELD_HEIGHT, FIELD_WIDTH, ActionType.WELL);
                gameboard.getFields().add(aField);
            } else if (id == 32) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            }
            id++;
        }

        //ids 38 to 34
        column = 6;
        id = 38;
        for (row = 1; row <= 5; row++) {
            if (id == 36) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);

            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 43 to 39
        id = 43;
        row = 1;
        for (column = 1; column <= 5; column++) {
            if (id == 41) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);
            } else if (id == 42) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.MAZE);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 44 to 47
        id = 44;
        column = 1;
        for (row = 2; row <= 5; row++) {
            if (id == 45) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 48 to 51
        id = 48;
        row = 5;
        for (column = 2; column <= 5; column++) {
            if (id == 50) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 54 to 52
        id = 54;
        column = 5;
        for (row = 2; row <= 4; row++) {
            if (id == 52) {
                aField = new BoardField(id, GRAY_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.JAIL);
                gameboard.getFields().add(aField);
            } else if (id == 53) {
                aField = new BoardField(id, ORANGE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.DICE);
                gameboard.getFields().add(aField);
            } else if (id == 54) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);

            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 57 to 55
        id = 57;
        row = 2;
        for (column = 2; column <= 4; column++) {
            gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id--;
        }
        //ids 58 to 59
        id = 58;
        column = 2;
        for (row = 3; row <= 4; row++) {
            if (id == 58) {
                aField = new BoardField(id, BLACK_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.DEATH);
                gameboard.getFields().add(aField);
            } else if (id == 59) {
                aField = new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE);
                gameboard.getFields().add(aField);
            } else {
                gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 60 to 61
        id = 60;
        row = 4;
        for (column = 3; column <= 4; column++) {
            gameboard.getFields().add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id++;
        }

        //id 62
        createSimpleField(gameboard, 62, 4, 3, WHITE_COLOR, FieldType.SQUARE);

        //id 63
        createSimpleField(gameboard, GOAL_FIELD,    3, 3, BROWN_COLOR, FieldType.END);

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

        goosPos.add(GOAL_FIELD);
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
