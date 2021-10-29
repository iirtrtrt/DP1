package org.springframework.samples.petclinic.game;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.enums.GameStatus;

import java.util.ArrayList;
import java.util.List;


public interface GameRepository extends CrudRepository<Game, Integer> {


    public List<Game> findByStatus(@Param("status") GameStatus status) throws DataAccessException;




}
