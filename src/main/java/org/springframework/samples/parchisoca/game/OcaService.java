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

    public static final Integer FIELD_WIDTH  = 1;
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
        gameBoard.height = 816;
        gameBoard.width = 816;

        //Create Game fields
        System.out.println("creating gameFields");

        gameBoard.fields = new ArrayList<BoardField>();
        this.createGameFields(gameBoard.fields);
        System.out.println("finished creating gameFields");





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
        int column;
        int row;

        //ids 0 to 7
        id=0;
        row = 7;

        for(column = 0; column <= 7; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //ids 14 to 8
        column = 7;
        id = 14;
        for(row = 0; row <= 6; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }

        //ids 21 to 15

        id=21;
        row = 0;
        for(column = 0; column <= 6; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }


        //ids 22 to 27
        column = 0;
        id = 22;
        for(row = 1; row <= 6; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //ids 28 to 33
        row = 6;
        id = 28;
        for(column = 1; column <= 6; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));
            id++;
        }

        //ids 38 to 34
        column = 6;
        id = 38;
        for(row = 1; row <= 5; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }

        //ids 43 to 39

        id=43;
        row = 1;
        for(column = 1; column <= 5; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }

        //ids 44 to 47

        id=44;
        column = 1;
        for(row = 2; row <= 5; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //ids 48 to 51

        id=48;
        row = 5;
        for(column = 2; column <= 5; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //ids 54 to 52

        id=54;
        column = 5;
        for(row = 2; row <= 4; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }

        //ids 57 to 55

        id=57;
        row = 2;
        for(column = 2; column <= 4; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id--;
        }
        //ids 58 to 59

        id=58;
        column = 2;
        for(row = 3; row <= 4; row++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }
        //ids 60 to 61

        id=60;
        row = 4;
        for(column = 3; column <= 4; column++) {
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));
            id++;
        }

        //id 62
        id=62;
        column=3;
        row=3;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, 2, FIELD_HEIGHT ));

        
    }
    @Transactional
    public void saveOca(Oca oca) throws DataAccessException {
        ocaRepo.save(oca);
    }
}
