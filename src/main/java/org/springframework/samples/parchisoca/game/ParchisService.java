package org.springframework.samples.parchisoca.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ParchisService {

    @Autowired
    ParchisRepository parchisRepo;

    @Autowired
    GameService gameService;

    @Autowired
    BoardFieldRepository boardFieldRepository;

    @Autowired
    BoardFieldService boardFieldService;

    GameRepository gameRepository;

    @Autowired
    OptionService optionService;

    @Autowired
    UserService userService;

    GameBoardRepository gameBoardRepository;
    public static final String STANDARD_FILL_COLOR = "#fef9e7";
    public static final String GREEN_END = "#26ca0c";
    public static final String RED_END = "#e32908";
    public static final String BLUE_END = "#0890e3";
    public static final String YELLOW_END = "#dbe117";

    public static final Integer FIELD_WIDTH = 2;
    public static final Integer FIELD_HEIGHT = 1;

    public Optional < Parchis > findById(Integer id) {
        return parchisRepo.findById(id);
    }



    @Autowired
    public ParchisService(ParchisRepository parchisRepository,
        GameRepository gameRepository, GameBoardRepository gameBoardRepository, BoardFieldRepository boardRepo, BoardFieldService boardFieldService,
        UserService userService, OptionService optionservice, GameService gameservice) {
        this.parchisRepo = parchisRepository;
        this.gameRepository = gameRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.boardFieldRepository = boardRepo;
        this.boardFieldService = boardFieldService;
        this.userService = userService;
        this.gameService = gameservice;
        this.optionService = optionservice;
    }

    public void initGameBoard(Game game) {
        //Todo: should not be hard coded
        Parchis gameBoard = new Parchis();
        gameBoard.background = "resources/images/background_board.jpg";
        gameBoard.height = 800;
        gameBoard.width = 800;

        //Create Game fields
        System.out.println("creating gameFields");

        gameBoard.fields = new ArrayList < BoardField > ();
        this.createGameFields(gameBoard);
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
    public Map<User,Integer> turns(Game game,Map<User,Integer> turns){
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
            turns.put(game.getCurrent_player(), game.getDice());
            game.setTurn_state(TurnState.CHOOSEPLAY);
            turns(game, turns);
        break;
        case CHOOSEPLAY:
            Parchis parchisOptions = (Parchis) game.getGameboard();
            parchisOptions.options = new ArrayList<>();
                
            Option option = new Option();
            option.setNumber(1);
            option.setText("Pass turn");
            optionService.saveOption(option);
            parchisOptions.options.add(option);
        break;
        case MOVE:
            Parchis parchisBoard = (Parchis) game.getGameboard();
                
            BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
            GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
            for (Option opt: ((Parchis) game.getGameboard()).options) {
                if (opt.getChoosen()) {
                    System.out.println("The Choice is: " + opt.getText());
                    fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
                }
            }
                
            game.setTurn_state(TurnState.NEXT);
            turns(game,turns);
        break;

        case NEXT:
            int index_last_player = game.getCurrent_players().indexOf(game.getCurrent_player());
            System.out.println("Index of current player" + game.getCurrent_player().getUsername() + ": " + index_last_player);
            System.out.println("Size of List: " + game.getCurrent_players().size());


            if (index_last_player == game.getCurrent_players().size() - 1) {
                //next player is the first one in the list
                Map<User,Integer> mapaOrdenado = turns.entrySet().stream()
                    .sorted((Map.Entry.<User,Integer>comparingByValue().reversed()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2)->e1, LinkedHashMap::new));
                List<User> listaOrdenado = mapaOrdenado.keySet().stream().collect(Collectors.toList());
                game.setCurrent_player(listaOrdenado.get(0));
                System.out.println("Current player after setting if: " + game.getCurrent_player().getUsername());

            } else {
                //next player is the next one in the list
                game.setCurrent_player(game.getCurrent_players().get(index_last_player + 1));
                System.out.println("Current player after setting else: " + game.getCurrent_player().getUsername());
            }
            game.setTurn_state(TurnState.INIT);
            System.out.println("Current player after setting " + game.getCurrent_player().getUsername());

            userService.getCurrentUser().get().setMyTurn(false);
            turns(game,turns);
        break;

        }return turns;
    }

    public void handleState(Game game, List<User> valuesPerPlayer) {
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
                game.setTurn_state(TurnState.CHOOSEPLAY);
                
                handleState(game, valuesPerPlayer);
                break;

           
                
            
            //SPECIAL roldice FOR WHEN YOU KILL SOMEONE
            // case SPECIALROLLDICE :
            //     game.rollDice();
            //     game.setDice(20);
            //     System.out.println("Dice Rolled: " + game.dice);
            //     game.setTurn_state(TurnState.CHOOSEPLAY);
            //     handleState(game);
            //     break;
            case CHOOSEPLAY:
                System.out.println("Choose Play!");
                Parchis parchis = (Parchis) game.getGameboard();
                parchis.options = new ArrayList<>();
                BoardField startField = null;
                Color currentColor = game.getCurrent_player().getGamePieces().get(0).getTokenColor();
                if(currentColor.equals(Color.GREEN)) startField = boardFieldService.find(56, game.getGameboard());          
                else if(currentColor.equals(Color.RED)) startField = boardFieldService.find(39, game.getGameboard());
                else if(currentColor.equals(Color.BLUE)) startField = boardFieldService.find(22, game.getGameboard());
                else if(currentColor.equals(Color.YELLOW)) startField = boardFieldService.find(5, game.getGameboard());
                optionCreator(game.getCurrent_player().getGamePieces(), game);
                if(parchis.getOptions().size() == 0){
                    if(game.getDice()<5){
                       Option op = new Option();
                        op.setNumber(1);
                        op.setText("Pass turn");
                        optionService.saveOption(op);
                        parchis.options.add(op);
                    } else if (game.getDice() == 5) {
                        Option op = new Option();
                        op.setNumber(1);
                        op.setText("Move piece from home");
                        optionService.saveOption(op);
                        parchis.options.add(op); 
                    }else if(game.getDice() == 6){
                        Option op = new Option();
                        op.setNumber(1);
                        op.setText("Repeat turn");
                        optionService.saveOption(op);
                        parchis.options.add(op);
                    }
                    
                }else if(game.getDice()==5 && parchis.getOptions().size() < 4 && startFieldAvailable(startField, game.getCurrent_player().getGamePieces().get(0).getTokenColor() )){ //If this fulfills you have to move a piece from home to start
                    parchis.options = new ArrayList<>();
                    Option op = new Option();
                    op.setNumber(1);
                    op.setText("Move piece from home");
                    optionService.saveOption(op);
                    parchis.options.add(op);
                } else if(game.getDice()==6 && parchis.getRepetitions()!=null){
                    if(parchis.getRepetitions()==2){
                        parchis.options = new ArrayList<>();
                        Option op = new Option();
                        op.setNumber(1);
                        op.setText("Lose piece");
                        optionService.saveOption(op);
                        parchis.options.add(op);  
                    }
                    
                } 
                //else if(game.getDice() == 20){
                //     parchis.options = new ArrayList<>();
                //     Option op = new Option();
                //     op.setNumber(1);
                //     op.setText("Move 20");
                //     optionService.saveOption(op);
                //     parchis.options.add(op);
                // }
                break;
            case MOVE:
                GamePiece selec = getMovingPiece(game);
                // Moves piece from home if possible
                Parchis parchisBoard = (Parchis) game.getGameboard();

                if (parchisBoard.getOptions().get(0).getText().equals("Move piece from home")) {
                    BoardField dependant = null;
                    for (GamePiece piece: game.getCurrent_player().getGamePieces()) {
                        if (piece.getField() == null) {
                            if (piece.getTokenColor().equals(Color.GREEN)) dependant = boardFieldService.find(56, game.getGameboard());
                            else if (piece.getTokenColor().equals(Color.RED)) dependant = boardFieldService.find(39, game.getGameboard());
                            else if (piece.getTokenColor().equals(Color.BLUE)) dependant = boardFieldService.find(22, game.getGameboard());
                            else if (piece.getTokenColor().equals(Color.YELLOW)) dependant = boardFieldService.find(5, game.getGameboard());
                            dependant.getListGamesPiecesPerBoardField().add(piece);
                            piece.setField(dependant);

                            break;
                        }
                    }
                    //Normal movement
                } else if (game.getDice() != 6 && !parchisBoard.getOptions().get(0).getText().equals("Pass turn")) {

                    Integer nextPos =  calcPosition(selec, game.getDice());
                    kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
                    movePiece(nextPos, selec, game);

                    
                //If dice = 6 normal movement + repeate turn
                } else if (game.getDice() == 6) {
                    
                    if (parchisBoard.getOptions().get(0).getText().equals("Repeat turn")) {
                        game.setTurn_state(TurnState.INIT);
                        handleState(game, valuesPerPlayer);
                        break;
                    } else {
                        
                        if (parchisBoard.getOptions().get(0).getText().equals("Lose piece")){
                            List<GamePiece> gamePieces = (game.getCurrent_player().getGamePieces());
                            Collections.shuffle(gamePieces);
                            for (GamePiece piece: gamePieces){
                                if (piece.getField() != null){
                                    piece.getField().getListGamesPiecesPerBoardField().remove(piece);
                                    piece.setField(null);
                                    parchisBoard.setRepetitions(0);
                                    game.setTurn_state(TurnState.NEXT);
                                    handleState(game, valuesPerPlayer);
                                    break;
                                }
                            }
                        }else{
                            Integer reps = parchisBoard.getRepetitions();
                            Integer nextPos =  calcPosition(selec, game.getDice());
                            kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
                            movePiece(nextPos, selec, game);
                            if(reps==null){
                              parchisBoard.setRepetitions(1);  
                            } else{
                              parchisBoard.setRepetitions(reps+1);  
                            }
                            
                            game.setTurn_state(TurnState.INIT);
                            handleState(game, valuesPerPlayer);
                            break;  
                        }
                        

                    }

                }
                game.setTurn_state(TurnState.NEXT);
                handleState(game, valuesPerPlayer);
                break;

            case NEXT:
                //get the player whos turn is next (simulate a loop)
                int index_last_player = valuesPerPlayer.indexOf(game.getCurrent_player());
                System.out.println("Index of current player:" + index_last_player);
                System.out.println("Size of List: " + game.getCurrent_players().size());

                if (index_last_player == valuesPerPlayer.size() - 1) {
                    //next player is the first one in the list
                    game.setCurrent_player(valuesPerPlayer.get(0));
                    System.out.println("Current player after setting if: " + game.getCurrent_player().getUsername());

                } else {
                    //next player is the next one in the list
                    game.setCurrent_player(valuesPerPlayer.get(index_last_player + 1));
                    System.out.println("Current player after setting else: " + game.getCurrent_player().getUsername());
                }
                game.setTurn_state(TurnState.INIT);
                System.out.println("Current player after setting " + game.getCurrent_player().getUsername());

                userService.getCurrentUser().get().setMyTurn(false);
                handleState(game, valuesPerPlayer);
                break;
            }    
        System.out.println(game.getTurn_state());  
    }



         




    public void setNextFields(GameBoard board){
        for(BoardField field : board.getFields()){
            BoardField next = null;
            if (field.getNumber() == 68) next = boardFieldService.find(1, board);
            else if (field.getNumber() == 174 || field.getNumber() == 157 || field.getNumber() == 140 || field.getNumber() == 123) {} else next = boardFieldService.find(field.getNumber() + 1, board);
            field.setNext_field(next);
        }


    }

    //Calculates all the Board Field entities that are needed
    public void createGameFields(GameBoard board) {
        int id;
        int column = 7;
        int row = 0;


        // BoardField[][] field_array = new BoardField[20][20];  unfortunately this does not work with oneToMany relationship

        //create all base fields

        //ids 35 to 43 and 59 to 67
        id = 35;
        for (row = 0; row < 20; row++) {
            if (row == 9 || row == 10) {
                id = 59;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id++;
        }

        //fields 34 and 68
        column = 9;
        row = 0;
        id = 34;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
        row = 19;
        id = 68;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));


        //ids 1-9 and 25-33
        column = 11;
        id = 33;
        for (row = 0; row < 20; row++) {
            if (row == 9 || row == 10) {
                id = 9;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id--;
        }

        //ids 50 to 44 and 24 to 18
        row = 7;
        id = 50;
        for (column = 0; column < 20; column++) {
            if (column > 6 && column < 13) {
                id = 24;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            id--;
        }

        //ids 52 to 58 and 10 to 16
        row = 11;
        id = 52;
        for (column = 0; column < 20; column++) {
            if (column > 6 && column < 13) {
                id = 10;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            id++;
        }

        //ids 51 and 17
        column = 0;
        row = 9;
        id = 51;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR,FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
        column = 19;
        id = 17;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));


        //create the end fields

        //green end fields
        row = 9;
        id = 151; //Todo: not sure what ids for the end fields
        for (column = 1; column < 8; column++) {
            board.fields.add(new BoardField(id, GREEN_END, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            id++;
        }


        //blue end fields
        row = 9;
        id = 123; //Todo: not sure what ids for the end fields
        for (column = 12; column < 19; column++) {
            board.fields.add(new BoardField(id, BLUE_END, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH));
            id--;
        }


        //ids red end fields
        column = 9;
        id = 134;
        for (row = 1; row < 8; row++) {
            board.fields.add(new BoardField(id, RED_END, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id++;
        }


        //ids yellow end fields
        column = 9;
        id = 174;
        for (row = 12; row < 19; row++) {
            board.fields.add(new BoardField(id, YELLOW_END, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT));
            id--;
        }

    }

    private void optionCreator(List <GamePiece> pieces, Game game) {
        Parchis parchis = (Parchis) game.getGameboard();
        for (GamePiece piece: pieces) {
            if (piece.getField() !=null) {
                Integer nextPos = calcPosition(piece, game.getDice());
                BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
                Boolean isBlocked = false;
                //Calculates if there are 2 pieces in a same field in the fields between the actual position of the piece and the supposed next position
                for(int i = piece.getField().getNext_field().getNumber(); i<=nextField.getNumber() ;i++){
                    BoardField midField = boardFieldService.find(i, game.getGameboard());
                    if (midField.getListGamesPiecesPerBoardField().size()==2) isBlocked = true;
                }
                //Movement only possible 
                // if(!isBlocked && (nextField.getListGamesPiecesPerBoardField().size() ==0 || (nextField.getListGamesPiecesPerBoardField().size()==1 && 
                // (nextField.getListGamesPiecesPerBoardField().get(0).getTokenColor().equals(piece.getTokenColor()) || nextField.getNumber()== 5 || nextField.getNumber()== 12 || 
                // nextField.getNumber()== 17 || nextField.getNumber()== 22 || nextField.getNumber()== 29 || nextField.getNumber()== 34 || nextField.getNumber()== 39 || 
                // nextField.getNumber()== 46 || nextField.getNumber()== 51 || nextField.getNumber()== 56 || nextField.getNumber()== 63 || nextField.getNumber()== 68 ))))
                if(!isBlocked && nextField.getListGamesPiecesPerBoardField().size()<2){ 
                    Integer fieldNumber = piece.getField().getNumber();
                    Option op = new Option();
                    op.setNumber(fieldNumber);
                    op.setText("Move piece in field " + String.valueOf(fieldNumber));
                    optionService.saveOption(op);
                    parchis.options.add(op);
                }
            }
        }
    }

    private Boolean startFieldAvailable (BoardField field, Color color){
        Boolean res = false;
        if(field.getListGamesPiecesPerBoardField().size()<2){
            res = true;
        }else{
            for(GamePiece piece: field.getListGamesPiecesPerBoardField()){
                if(!piece.getTokenColor().equals(color)){
                    piece.setField(null);
                    field.getListGamesPiecesPerBoardField().remove(field);
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    private void kickPiece (BoardField field, GamePiece piece, Game game){
        if (field.getListGamesPiecesPerBoardField().size()==1 && !(field.getNumber()== 5 || field.getNumber()== 12
        || field.getNumber()== 17 || field.getNumber()== 22 || field.getNumber()== 29 || field.getNumber()== 34 ||
        field.getNumber()== 39 || field.getNumber()== 46 || field.getNumber()== 51 || field.getNumber()== 56 || field.getNumber()== 63 || field.getNumber()== 68 || 
        field.getListGamesPiecesPerBoardField().get(0).getTokenColor().equals(piece.getTokenColor()))){
            GamePiece pieceInField = field.getListGamesPiecesPerBoardField().get(0);
            if(!pieceInField.getTokenColor().equals(piece.getTokenColor())) pieceInField.setField(null);
            field.getListGamesPiecesPerBoardField().remove(pieceInField);
            movePiece20(game);
        }
    }

    private Integer calcPosition(GamePiece piece, Integer moves){
        Integer x = piece.getField().getNext_field().getNumber();
        Integer nextPos =  (x+moves-1)%68;
        if(nextPos>= 1 && nextPos<= 6 && piece.getField().getNumber()<=68 && piece.getField().getNumber()>=48 && piece.getTokenColor().equals(Color.YELLOW) ) nextPos = nextPos + 168-1;
        else if(nextPos>= 52 && nextPos<= 57 && piece.getField().getNumber()<=51 && piece.getField().getNumber()>=31 && piece.getTokenColor().equals(Color.GREEN) ) nextPos = nextPos - 51 + 151-1;
        else if(nextPos>= 35 && nextPos<= 40 && piece.getField().getNumber()<=34 && piece.getField().getNumber()>=14 && piece.getTokenColor().equals(Color.RED) ) nextPos = nextPos - 34 + 134-1;
        else if(nextPos>= 18 && nextPos<= 23 && ((piece.getField().getNumber()<=17 && piece.getField().getNumber()>=12) || (piece.getField().getNumber()<=65 && piece.getField().getNumber()>=68)) && piece.getTokenColor().equals(Color.BLUE) ) nextPos = nextPos - 17 + 117-1;
        return nextPos;
    }   
    private void movePiece (Integer nextPos, GamePiece piece, Game game) {
        BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
        // Boolean isBlocked = false;
        // //Calculates if there are 2 pieces in a same field in the fields between the actual position of the piece and the supposed next position
        // for(int i = piece.getField().getNext_field().getNumber(); i<=nextField.getNumber() ;i++){
        //     BoardField midField = boardFieldService.find(i, game.getGameboard());
        //     if (midField.getListGamesPiecesPerBoardField().size()==2) isBlocked = true;
        // }
        // //The piece moves if there are no pieces in the next field, if there is one but that piece is the same color, if there is one piece of another color but is a 
        // //safe field and if there is not a block between the current and next fields
        // if(!isBlocked && (nextField.getListGamesPiecesPerBoardField().size() ==0 || (nextField.getListGamesPiecesPerBoardField().size()==1 && 
        // (nextField.getListGamesPiecesPerBoardField().get(0).getTokenColor().equals(piece.getTokenColor()) || nextField.getNumber()== 5 || nextField.getNumber()== 12 || 
        // nextField.getNumber()== 17 || nextField.getNumber()== 22 || nextField.getNumber()== 29 || nextField.getNumber()== 34 || nextField.getNumber()== 39 || 
        // nextField.getNumber()== 46 || nextField.getNumber()== 51 || nextField.getNumber()== 56 || nextField.getNumber()== 63 || nextField.getNumber()== 68 )))){    
            piece.getField().getListGamesPiecesPerBoardField().remove(piece);
            if(nextField.getListGamesPiecesPerBoardField().size()==0){
                nextField.setListGamesPiecesPerBoardField(new ArrayList<GamePiece>()); 
                nextField.getListGamesPiecesPerBoardField().add(piece);  
            }else{
                GamePiece pieceInField = nextField.getListGamesPiecesPerBoardField().get(0);  
                nextField.setListGamesPiecesPerBoardField(new ArrayList<GamePiece>()); 
                nextField.getListGamesPiecesPerBoardField().add(pieceInField);  
                nextField.getListGamesPiecesPerBoardField().add(piece);  
            }
            piece.setField(nextField);
            
        //}
        
    }

    private GamePiece getMovingPiece (Game game){
        BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
        GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
        for (Option opt: ((Parchis) game.getGameboard()).options) {
            if (opt.getChoosen()) {
                System.out.println("The Choice is: " + opt.getText());
                fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
            }
        }
        for (GamePiece piece: game.getCurrent_player().getGamePieces()) {
            if (piece.getField() == fieldSelec) selec = piece;
        }
        return selec;
    }

    private void movePiece20 (Game game){
        optionCreator(game.getCurrent_player().getGamePieces(), game);
        GamePiece selec = getMovingPiece(game);
        Integer nextPos =  calcPosition(selec, game.getDice());
        kickPiece(boardFieldService.find(nextPos, game.getGameboard()), selec, game);
        movePiece(nextPos, selec, game);
    }


    @Transactional
    public void saveParchis(Parchis parchis) throws DataAccessException {
        parchisRepo.save(parchis);
    }





}