package org.springframework.samples.parchisoca.game;

import org.springframework.data.repository.CrudRepository;
import org.springframework.dao.DataAccessException;

public interface ParchisRepository extends CrudRepository<Parchis, Integer>{

    Parchis findById(int id) throws DataAccessException;


    //Method for getting all parchis games to be able to display all and choose which one to join (dont know if neccesary)
    Iterable<Parchis> findAll() throws DataAccessException;

}
