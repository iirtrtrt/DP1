package org.springframework.samples.parchisoca.repository;

import org.springframework.data.repository.CrudRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.model.game.Oca;

public interface OcaRepository extends CrudRepository <Oca, Integer > {

    Oca findById(int id) throws DataAccessException;

    //Method for getting all oca games to be able to display all and choose which one to join (dont know if neccesary)
    Iterable < Oca > findAll() throws DataAccessException;
}
