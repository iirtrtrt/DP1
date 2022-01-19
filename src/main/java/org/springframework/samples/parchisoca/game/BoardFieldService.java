package org.springframework.samples.parchisoca.game;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataAccessException;

@Service
public class BoardFieldService {

    @Autowired
    BoardFieldRepository boardFieldRepo;

    public static final String STANDARD_FILL_COLOR = "#fef9e7";
    public static final String GREEN_END = "#26ca0c";
    public static final String RED_END = "#e32908";
    public static final String BLUE_END = "#0890e3";
    public static final String YELLOW_END = "#dbe117";

    public static final Integer FIELD_WIDTH = 2;
    public static final Integer FIELD_HEIGHT = 1;

    public Optional<BoardField> findById(Integer id) {
        return boardFieldRepo.findById(id);
    }

    @Autowired
    public BoardFieldService(BoardFieldRepository boardFieldRepository) {
        this.boardFieldRepo = boardFieldRepository;
    }

    /**
     * saves a parchis or oca board to the database
     */
    @Transactional
    public void saveBoardField(BoardField field) throws DataAccessException {
        boardFieldRepo.save(field);
    }

    public BoardField find(Integer number, GameBoard board) {
        return this.boardFieldRepo.findByNumberAndBoard(number, board);
    }

    public BoardField getNext_fieldByNumberAndBoard(Integer i, GameBoard gameBoard) {
        return this.boardFieldRepo.findByNumberAndBoard(i, gameBoard).next_field;
    }
}
