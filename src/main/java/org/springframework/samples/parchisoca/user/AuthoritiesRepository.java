package org.springframework.samples.parchisoca.user;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.parchisoca.game.Option;

import java.util.Optional;


public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{

    Optional<Authorities> findByUser(@Param("user") User user) throws DataAccessException;

}
