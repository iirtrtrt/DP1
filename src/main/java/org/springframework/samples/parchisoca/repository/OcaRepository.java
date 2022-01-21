package org.springframework.samples.parchisoca.repository;

import org.springframework.data.repository.CrudRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.model.game.Oca;

public interface OcaRepository extends CrudRepository <Oca, Integer > {

    Oca findById(int id) throws DataAccessException;

    Iterable < Oca > findAll() throws DataAccessException;
}
