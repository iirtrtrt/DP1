package org.springframework.samples.parchisoca.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ParchisService {

    @Autowired
	ParchisRepository parchisRepo;

    @Autowired
    BoardFieldRepository boardFieldRepository;

    GameRepository gameRepository;

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
                      GameRepository gameRepository, GameBoardRepository gameBoardRepository, BoardFieldRepository boardRepo) {
        this.parchisRepo = parchisRepository;
        this.gameRepository = gameRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.boardFieldRepository = boardRepo;
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
        this.createGameFields(gameBoard.fields);
        System.out.println("finished creating gameFields");

        //The following code is only for testing purposes until "Join"-function exists
        GamePiece gamepiece = new GamePiece();
        gamepiece.setTokenColor(Color.BLUE);
        gamepiece.setField(gameBoard.fields.get(20));
        GamePiece gamepiece2 = new GamePiece();
        gamepiece2.setTokenColor(Color.GREEN);

        List <GamePiece> pieces = new ArrayList<GamePiece>();
        pieces.add(gamepiece);
        pieces.add(gamepiece2);
        User user = new User();
        user.setGamePieces(pieces);
        System.out.println("finished setting game Pieces");
        System.out.println(user.getGamePieces().size());


        List<User> user_list = new ArrayList<User>();
        user_list.add(user);
        game.setOther_players(user_list);

        //game.setOther_players(user_list);
        //The following code is only for testing purposes until "Join"-function exists

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




    }

    //Calculates all the Board Field entities that are needed
    public void createGameFields(List<BoardField> fields){
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
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //fields 34 and 68
        column = 9;
        row = 0;
        id = 34;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
        row = 19;
        id = 68;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));


        //ids 1-9 and 25-33
        column = 11;
        id = 33;
        for(row = 0; row < 20; row++) {
            if(row == 9 || row == 10){
                id = 9;
                continue;
            }
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
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
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
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
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id++;
        }

        //ids 51 and 17
        column = 0;
        row = 9;
        id = 51;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
        column = 19;
        id = 17;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));


        //create the end fields

        //green end fields
        row =  9;
        id = 70; //Todo: not sure what ids for the end fields
        for(column = 1; column < 8; column++) {
            fields.add(new BoardField(id, GREEN_END, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id++;
        }


         //green end fields
         row =  9;
         id = 90; //Todo: not sure what ids for the end fields
         for(column = 12; column < 19; column++) {
             fields.add(new BoardField(id, BLUE_END, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
             id++;
         }


         //ids red end fields
        column = 9;
        id = 80;
        for(row = 1; row < 8; row++) {
            fields.add(new BoardField(id, RED_END, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }


        //ids red end fields
        column = 9;
        id = 100;
        for(row = 12; row < 19; row++) {
            fields.add(new BoardField(id, YELLOW_END, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

/*
        for (BoardField field : fields){
            boardFieldRepository.save(field);
        }*/

    }
    @Transactional
    public void saveParchis(Parchis parchis) throws DataAccessException {
        parchisRepo.save(parchis);
    }





}
