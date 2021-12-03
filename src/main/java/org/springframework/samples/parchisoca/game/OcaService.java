
package org.springframework.samples.parchisoca.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OcaService {

    @Autowired
    OcaRepository ocaRepo;
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
    UserService userService;

    public static final String WHITE_COLOR = "#FFFFFF"; //basic
    public static final String YELLOW_COLOR = "#FFFF00"; // goose
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
        return ocaRepo.findById(id);
    }

    @Autowired
    public OcaService(OcaRepository ocaRepository, GameRepository gameRepository, GameBoardRepository gameBoardRepository, BoardFieldRepository boardRepo,
        GameService gameService, BoardFieldService boardFieldService, UserService userService) {
        this.ocaRepo = ocaRepository;
        this.gameRepository = gameRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.boardFieldRepository = boardRepo;
        this.boardFieldService = boardFieldService;
        this.userService = userService;
        this.gameService = gameService;
        this.boardFieldService = boardFieldService;
        this.userService = userService;
    }

    public void initGameBoard(Game game) {
        Oca gameBoard = new Oca();
        gameBoard.height = 800;
        gameBoard.width = 800;

        //Create Game fields
        System.out.println("creating gameFields");

        gameBoard.fields = new ArrayList < BoardField > ();
        this.createGameFields(gameBoard.fields);
        System.out.println("finished creating gameFields");

        System.out.println("setting gameboard");
        gameBoard.setGame(game);
        game.setGameboard(gameBoard);

        try {
            this.gameBoardRepository.save(gameBoard);
        } catch (Exception e) {
            System.out.println("exception: " + e.getMessage());
        }

        for (BoardField field: gameBoard.getFields()) {
            field.setBoard(gameBoard);
            boardFieldService.saveBoardField(field);
        }

        
    }

    public void setNextFields(GameBoard board) {
        for (BoardField field: board.getFields()) {
            BoardField next = null;
            if (field.getNumber() != 63){
                next = boardFieldService.find(field.getNumber() + 1, board);
                field.setNext_field(next);
            }
        }
    }

    public void handleState(Game game) {
        setNextFields(game.getGameboard());
        switch (game.getTurn_state()) {
            case INIT:
                System.out.println("Current Player in Init: " + game.getCurrent_player().getUsername());
                if (game.getCurrent_player() == userService.getCurrentUser().get()) {
                    userService.getCurrentUser().get().setMyTurn(true);
                    System.out.println("The current user has been found:");
                }
                break;
            case ROLLDICE:
                game.rollDice();
                System.out.println("Dice Rolled: " + game.dice);
                //Implement the actual move here!

                game.setTurn_state(TurnState.CHOOSEPLAY);
                handleState(game);
                break;

            case CHOOSEPLAY:
                System.out.println("Choose Play!");
                Oca oca = (Oca) game.getGameboard();
                oca.options = new ArrayList<>();
                BoardField startField = boardFieldService.find(1, game.getGameboard());
                optionCreator2(game.getCurrent_player().getGamePieces(), oca);
                
                Option op = new Option();
                op.setNumber(1);
                op.setText("Move piece");
                optionService.saveOption(op);
                oca.options.add(op);
                game.setTurn_state(TurnState.MOVE);
                handleState(game);
                
            
            case NEXT:
                int index_last_player = game.getCurrent_players().indexOf(game.getCurrent_player());
                System.out.println("Index of current player" + game.getCurrent_player().getUsername() + ": " + index_last_player);
                System.out.println("Size of List: " + game.getCurrent_players().size());

                if (index_last_player == game.getCurrent_players().size() - 1) {
                    //next player is the first one in the list
                    game.setCurrent_player(game.getCurrent_players().get(0));
                    System.out.println("Current player after setting if: " + game.getCurrent_player().getUsername());

                } else {
                    //next player is the next one in the list
                    game.setCurrent_player(game.getCurrent_players().get(index_last_player + 1));
                    System.out.println("Current player after setting else: " + game.getCurrent_player().getUsername());
                }
                game.setTurn_state(TurnState.INIT);
                System.out.println("Current player after setting " + game.getCurrent_player().getUsername());

                userService.getCurrentUser().get().setMyTurn(false);
                handleState(game);
                break;
            case  MOVE:
                Oca ocaBoard =(Oca) game.getGameboard();
                BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
                GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
                BoardField dependant = boardFieldService.find(1, game.getGameboard());
                for (Option opt: ocaBoard.getOptions()) {
                    if (opt.getChoosen()) {
                        System.out.println("The Choice is: " + opt.getText());
                        fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
                    }
                }
                selec.setField(dependant); 
                Integer nextPos =  calcPosition2(selec, game);
                movePiece2(nextPos, selec, game);
                break;
        }
    }


    //Calculates all the Board Field entities that are needed
    public BoardField createGameFields(List < BoardField > fields) {
        int id;
        int column;
        int row;
        BoardField start_field = null;

        //ids 0 to 7
        id = 0;
        row = 7;
        for (column = 0; column <= 7; column++) {
            if (id == 0) {
                start_field = new BoardField(id, LIGHTBROWN_COLOR, FieldType.START, column, row, FIELD_WIDTH, FIELD_HEIGHT);
                fields.add(start_field);
            } else if (id == 5 || id == 1) {
                fields.add(new ActionField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.GOOSE));
            } else if (id == 6) {
                fields.add(new ActionField(id, BLUE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT, ActionType.BRIDGE));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 14 to 8
        column = 7;
        id = 14;
        for (row = 0; row <= 6; row++) {
            if (id == 9 || id == 14) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else if (id == 12) {
                fields.add(new BoardField(id, BLUE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 21 to 15
        id = 21;
        row = 0;
        for (column = 0; column <= 6; column++) {
            if (id == 18) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else if (id == 19) {
                fields.add(new BoardField(id, GRAY_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }


        //ids 22 to 27
        column = 0;
        id = 22;
        for (row = 1; row <= 6; row++) {
            if (id == 23 || id == 27) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else if (id == 26) {
                fields.add(new BoardField(id, ORANGE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 28 to 33
        row = 6;
        id = 28;
        for (column = 1; column <= 6; column++) {
            if (id == 31) {
                fields.add(new BoardField(id, GRAY_COLOR, FieldType.HORIZONTAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            } else if (id == 32) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            }
            id++;
        }

        //ids 38 to 34
        column = 6;
        id = 38;
        for (row = 1; row <= 5; row++) {
            if (id == 36) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 43 to 39
        id = 43;
        row = 1;
        for (column = 1; column <= 5; column++) {
            if (id == 41) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else if (id == 42) {
                fields.add(new BoardField(id, GRAY_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 44 to 47
        id = 44;
        column = 1;
        for (row = 2; row <= 5; row++) {
            if (id == 45) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 48 to 51
        id = 48;
        row = 5;
        for (column = 2; column <= 5; column++) {
            if (id == 50) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 54 to 52
        id = 54;
        column = 5;
        for (row = 2; row <= 4; row++) {
            if (id == 52) {
                fields.add(new BoardField(id, GRAY_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else if (id == 53) {
                fields.add(new BoardField(id, ORANGE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else if (id == 54) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id--;
        }

        //ids 57 to 55
        id = 57;
        row = 2;
        for (column = 2; column <= 4; column++) {
            fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id--;
        }
        //ids 58 to 59
        id = 58;
        column = 2;
        for (row = 3; row <= 4; row++) {
            if (id == 58) {
                fields.add(new BoardField(id, BLACK_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else if (id == 59) {
                fields.add(new BoardField(id, YELLOW_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            } else {
                fields.add(new BoardField(id, WHITE_COLOR, FieldType.VERTICAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            }
            id++;
        }

        //ids 60 to 61
        id = 60;
        row = 4;
        for (column = 3; column <= 4; column++) {
            fields.add(new BoardField(id, WHITE_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id++;
        }

        //id 62
        id = 62;
        column = 4;
        row = 3;
        fields.add(new BoardField(id, WHITE_COLOR, FieldType.SQUARE, column, row, FIELD_WIDTH, FIELD_HEIGHT));

        //id 63
        id = 63;
        column = 3;
        row = 3;
        fields.add(new BoardField(id, BROWN_COLOR, FieldType.END, column, row, FIELD_WIDTH, FIELD_HEIGHT));

        return start_field;
    }


    @Transactional
    public void saveOca(Oca oca) throws DataAccessException {
        ocaRepo.save(oca);
    }

    private Integer calcPosition2 (GamePiece piece, Game game){
        Integer x = piece.getField().getNext_field().getNumber();
        Integer nextPos =  (x+game.getDice()-1);
        return nextPos;
    }

    private void movePiece2(Integer nextPos, GamePiece piece, Game game){
        BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
        piece.getField().getListGamesPiecesPerBoardField().remove(piece);
        piece.setField(nextField);
    }
    private void optionCreator2(List<GamePiece> pieces, Oca oca) {
        for (GamePiece piece: pieces) {
            if (piece.getField() != null) {
                Integer fieldNumber = piece.getField().getNumber();
                Option op = new Option();
                op.setNumber(fieldNumber);
                op.setText("Move piece in field " + String.valueOf(fieldNumber));
                optionService.saveOption(op);
                oca.options.add(op);
            }
        }
    }
}
