package org.springframework.samples.parchisoca.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import java.util.Optional;

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
    public static final String STANDARD_FILL_COLOR  = "#fef9e7" ;
    public static final String GREEN_END  = "#26ca0c" ;
    public static final String RED_END  = "#e32908" ;
    public static final String BLUE_END  = "#0890e3" ;
    public static final String YELLOW_END = "#dbe117";

    public static final Integer FIELD_WIDTH  = 2;
    public static final Integer FIELD_HEIGHT  = 1;

    public Optional<Parchis> findById(Integer id){
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



    public void initGameBoard(Game game){
        //Todo: should not be hard coded
        Parchis gameBoard = new Parchis();
        gameBoard.background = "resources/images/background_board.jpg";
        gameBoard.height = 800;
        gameBoard.width = 800;

        //Create Game fields
        System.out.println("creating gameFields");

        gameBoard.fields = new ArrayList<BoardField>();
        this.createGameFields(gameBoard);
        System.out.println("finished creating gameFields");


        System.out.println("setting gameboard");
        gameBoard.setGame(game);
        game.setGameboard(gameBoard);


        try
        {
            this.gameBoardRepository.save(gameBoard);
        }
        catch (Exception e )
        {
            System.out.println("exception: " + e.getMessage());
        }

        for (BoardField field : gameBoard.getFields()){
            field.setBoard(gameBoard);
            boardFieldService.saveBoardField(field);
        }
    }

    public void handleState(Game game){
        
        

        setNextFields(game.getGameboard());
        switch(game.getTurn_state()){
            case INIT :
                System.out.println("Current Player in Init: " + game.getCurrent_player().getUsername());
                if(game.getCurrent_player() == userService.getCurrentUser().get()){
                    userService.getCurrentUser().get().setMyTurn(true);
                    System.out.println("The current user has been found:");
                }
                break;
            case ROLLDICE :
                game.rollDice();
                System.out.println("Dice Rolled: " + game.dice);
                game.setTurn_state(TurnState.CHOOSEPLAY);
                handleState(game);
                break;
            case CHOOSEPLAY:
                System.out.println("Choose Play!");
                Parchis parchis = (Parchis) game.getGameboard();
                parchis.options = new ArrayList<>();
                optionCreator(game.getCurrent_player().getGamePieces(), parchis);
                if(parchis.getOptions().size() == 0 || (game.getDice()==5 && parchis.getOptions().size() < 4 )){ //If this fulfills you have to move a piece from home to start
                    game.setTurn_state(TurnState.MOVE);
                    handleState(game);
                } else{
                    for(Option opt : ((Parchis) game.getGameboard()).options){
                        if(opt.getChoosen()){
                        System.out.println("The Choice is: " + opt.getText());
                        }
                    }
                }

                
                break;
            case MOVE:
                Parchis parchisBoard = (Parchis) game.getGameboard();
                Integer repetitions = 0;
                if (game.getDice()==5 && parchisBoard.getOptions().size() < 4){
                    
                    BoardField dependant=null;
                    for (GamePiece piece: game.getCurrent_player().getGamePieces()){
                        if(piece.getField() == null){
                            if(piece.getTokenColor().equals(Color.GREEN)) dependant = boardFieldService.find(56, game.getGameboard());
                            else if(piece.getTokenColor().equals(Color.RED)) dependant = boardFieldService.find(39, game.getGameboard());
                            else if(piece.getTokenColor().equals(Color.BLUE)) dependant = boardFieldService.find(22, game.getGameboard());
                            else if(piece.getTokenColor().equals(Color.YELLOW)) dependant = boardFieldService.find(5, game.getGameboard());
                            piece.setField(dependant); 
                            
                            break;
                        }
                    }
                    
                }else if(!parchisBoard.getOptions().isEmpty() && game.getDice()!=6){   
                    BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
                    GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
                    for(Option opt : ((Parchis) game.getGameboard()).options){
                        if(opt.getChoosen()){
                            System.out.println("The Choice is: " + opt.getText());
                            fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
                        }
                    }
                    for (GamePiece piece: game.getCurrent_player().getGamePieces()){
                        
                        if(piece.getField() == fieldSelec) selec = piece;
                    }
                    
                    Integer pos = selec.getField().getNext_field().getNumber();
                    Integer nextPos =  pos+game.getDice()-1;
                    if(nextPos> 68 ) nextPos =game.getDice() - (68-selec.getField().getNumber());
                    BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
                    selec.setField(nextField);
                }else if(game.getDice()==6){
                    repetitions +=1;
                    if(parchisBoard.getOptions().isEmpty()){
                        game.setTurn_state(TurnState.INIT);
                        handleState(game);  
                        break;
                    }else{
                        GamePiece last = game.getCurrent_player().getGamePieces().get(0);
                        if (repetitions <3){
                            BoardField fieldSelec = boardFieldService.find(1, game.getGameboard());
                            GamePiece selec = game.getCurrent_player().getGamePieces().get(0);
                            for(Option opt : ((Parchis) game.getGameboard()).options){
                                if(opt.getChoosen()){
                                    System.out.println("The Choice is: " + opt.getText());
                                    fieldSelec = boardFieldService.find(opt.getNumber(), game.getGameboard());
                                }
                            }
                            for (GamePiece piece: game.getCurrent_player().getGamePieces()){
                                if(piece.getField() == fieldSelec) selec = piece; last = piece;
                            }
                            Integer pos = selec.getField().getNext_field().getNumber();
                            Integer nextPos =  pos+game.getDice()-1;
                            if(nextPos> 68 ) nextPos =game.getDice() - (68-selec.getField().getNumber());
                            BoardField nextField = boardFieldService.find(nextPos, game.getGameboard());
                            selec.setField(nextField);
                            game.setTurn_state(TurnState.INIT);
                            handleState(game); 
                            break;
                        }else{
                            last.setField(null);
                            game.setTurn_state(TurnState.NEXT);
                            handleState(game); 
                            break;
                        }
                    }
                   
                }
                repetitions = 0;
                game.setTurn_state(TurnState.NEXT);
                handleState(game);
                break;
               
                
                
            case NEXT :
                //get the player whos turn is next (simulate a loop)
                int index_last_player = game.getCurrent_players().indexOf(game.getCurrent_player());
                System.out.println("Index of current player:" + index_last_player);
                System.out.println("Size of List: " + game.getCurrent_players().size());
                
                if(index_last_player == game.getCurrent_players().size() - 1){
                    //next player is the first one in the list 
                    game.setCurrent_player(game.getCurrent_players().get(0));
                    System.out.println("Current player after setting if: " + game.getCurrent_player().getUsername());

                }
                else{
                    //next player is the next one in the list
                    game.setCurrent_player(game.getCurrent_players().get(index_last_player + 1));
                    System.out.println("Current player after setting else: " + game.getCurrent_player().getUsername());
                }
                game.setTurn_state(TurnState.INIT);
                System.out.println("Current player after setting " + game.getCurrent_player().getUsername());

                userService.getCurrentUser().get().setMyTurn(false);
                handleState(game);
                break;
        }
        System.out.println(game.getTurn_state());

        
    }



    public void setNextFields(GameBoard board){
        for(BoardField field : board.getFields()){
            BoardField next = null;
            if(field.getNumber() == 68) next = boardFieldService.find(1, board);
            else if(field.getNumber() == 174 || field.getNumber() == 157 || field.getNumber() == 140 || field.getNumber() == 123){}
            else next = boardFieldService.find(field.getNumber() + 1, board);
            field.setNext_field(next);
        }
    }



    //Calculates all the Board Field entities that are needed
    public void createGameFields(GameBoard board){
        int id;
        int column = 7;
        int row = 0;

        // BoardField[][] field_array = new BoardField[20][20];  unfortunately this does not work with oneToMany relationship

        //create all base fields

        //ids 35 to 43 and 59 to 67
        id = 35;
        for(row = 0; row < 20; row++) {
            if(row == 9 || row == 10){
                id = 59;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //fields 34 and 68
        column = 9;
        row = 0;
        id = 34;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
        row = 19;
        id = 68;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT ));


        //ids 1-9 and 25-33
        column = 11;
        id = 33;
        for(row = 0; row < 20; row++) {
            if(row == 9 || row == 10){
                id = 9;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }

        //ids 50 to 44 and 24 to 18
        row = 7;
        id = 50;
        for(column = 0; column < 20; column++) {
            if(column > 6 && column < 13){
                id = 24;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id--;
        }

        //ids 52 to 58 and 10 to 16
        row = 11;
        id = 52;
        for(column = 0; column < 20; column++) {
            if(column > 6 && column < 13){
                id = 10;
                continue;
            }
            board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id++;
        }

        //ids 51 and 17
        column = 0;
        row = 9;
        id = 51;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR,FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
        column = 19;
        id = 17;
        board.fields.add(new BoardField(id, STANDARD_FILL_COLOR, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH ));


        //create the end fields

        //green end fields
        row =  9;
        id = 151; //Todo: not sure what ids for the end fields
        for(column = 1; column < 8; column++) {
            board.fields.add(new BoardField(id, GREEN_END, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id++;
        }


         //blue end fields
         row =  9;
         id = 123; //Todo: not sure what ids for the end fields
         for(column = 12; column < 19; column++) {
            board.fields.add(new BoardField(id, BLUE_END, FieldType.VERTICAL, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
             id--;
         }


         //ids red end fields
        column = 9;
        id = 134;
        for(row = 1; row < 8; row++) {
            board.fields.add(new BoardField(id, RED_END, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }


        //ids yellow end fields
        column = 9;
        id = 174;
        for(row = 12; row < 19; row++) {
            board.fields.add(new BoardField(id, YELLOW_END, FieldType.HORIZONTAL, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }

    }

    public void optionCreator (List<GamePiece> pieces, Parchis parchis){
        for(GamePiece piece : pieces){
            if (piece.getField() != null){
                Integer fieldNumber = piece.getField().getNumber();
                Option op = new Option();
                op.setNumber(fieldNumber);
                op.setText("Do you want to move piece in field " + String.valueOf(fieldNumber));
                optionService.saveOption(op);
                parchis.options.add(op);
            }            
        }  
    }
 
    @Transactional
    public void saveParchis(Parchis parchis) throws DataAccessException {
        parchisRepo.save(parchis);
    }





}
