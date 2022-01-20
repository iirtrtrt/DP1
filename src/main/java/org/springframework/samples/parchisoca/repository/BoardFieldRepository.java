package org.springframework.samples.parchisoca.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.parchisoca.model.game.BoardField;
import org.springframework.samples.parchisoca.model.game.GameBoard;

public interface BoardFieldRepository extends CrudRepository <BoardField, Integer > {

    public BoardField findByNumberAndBoard(@Param("number") Integer number, @Param("board") GameBoard board) throws DataAccessException;
}


