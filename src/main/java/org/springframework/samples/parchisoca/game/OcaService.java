package org.springframework.samples.parchisoca.game;

import org.springframework.samples.parchisoca.enums.FieldType;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import org.springframework.samples.parchisoca.user.User;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
public class OcaService {

    @Autowired 
	OcaRepository ocaRepo;

    @Autowired
    BoardFieldRepository boardFieldRepository;

    GameRepository gameRepository;

    GameBoardRepository gameBoardRepository;

    public static final String STANDARD_FILL_COLOR  = "#fef9e7" ;

    public static final Integer FIELD_WIDTH  = 2;
    public static final Integer FIELD_HEIGHT  = 1;

    public Optional<Oca> findById(Integer id){
		return ocaRepo.findById(id);
	}

    @Autowired
    public OcaService(OcaRepository ocaRepository,
                      GameRepository gameRepository, GameBoardRepository gameBoardRepository, BoardFieldRepository boardRepo) {
        this.ocaRepo = ocaRepository;
        this.gameRepository = gameRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.boardFieldRepository = boardRepo;
    }

    public void initGameBoard(Game game){
        //Todo: should not be hard coded
        Oca gameBoard = new Oca();
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
        gamepiece.setField(gameBoard.fields.get(12));
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
        int column = 9;
        int row = 0;

        // BoardField[][] field_array = new BoardField[20][20];  unfortunately this does not work with oneToMany relationship

        //create all base fields

        //ids 19 to 8
        id = 19;
        for(row = 0; row < 13; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }


        //ids 28 to 38
        column = 0;
        id = 28;
        for(row = 0; row < 12; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //ids 52 to 45
        column = 7;
        id = 52;
        for(row = 3; row < 11; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id--;
        }

        //ids 57 to 61
        column = 2;
        id = 57;
        for(row = 3; row < 8; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id++;
        }

        //ids 1 to 7
        row = 11;
        id = 0;
        for(column = 2; column < 9; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id++;
        }

        //ids 51 and 17
        /*column = 0;
        row = 9;
        id = 51;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
        column = 19;
        id = 17;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));*/


        //create the end fields

        //green end fields
       // row =  9;
        //id = 70; //Todo: not sure what ids for the end fields
        //for(column = 1; column < 8; column++) {
          //  fields.add(new BoardField(id, GREEN_END, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            //id++;
        //}


         //green end fields
         //row =  9;
         //id = 90; //Todo: not sure what ids for the end fields
         //for(column = 12; column < 19; column++) {
           //  fields.add(new BoardField(id, BLUE_END, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
             //id++;
         //}


         //ids red end fields
        //column = 9;
        //id = 80;
        //for(row = 1; row < 8; row++) {
          //  fields.add(new BoardField(id, RED_END, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            //id++;
        //}


        //ids red end fields
        //column = 9;
        //id = 100;
        //for(row = 12; row < 19; row++) {
          //  fields.add(new BoardField(id, YELLOW_END, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            //id++;
        //}
    }
    @Transactional
    public void saveOca(Oca oca) throws DataAccessException {
        ocaRepo.save(oca);
    }
}
