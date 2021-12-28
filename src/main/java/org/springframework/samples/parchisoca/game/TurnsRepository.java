package org.springframework.samples.parchisoca.game;


import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TurnsRepository extends CrudRepository<Turns, Integer>{

    Optional<Turns> findByUsername(@Param("username") String username) throws DataAccessException;

}
