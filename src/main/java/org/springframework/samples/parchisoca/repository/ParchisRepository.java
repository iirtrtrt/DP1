package org.springframework.samples.parchisoca.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.model.game.Parchis;

public interface ParchisRepository extends CrudRepository<Parchis, Integer>{

    Parchis findById(int id) throws DataAccessException;


    //Method for getting all parchis games to be able to display all and choose which one to join (dont know if neccesary)
    Iterable<Parchis> findAll() throws DataAccessException;

}
