package org.springframework.samples.parchisoca.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.parchisoca.model.user.Authorities;
import org.springframework.samples.parchisoca.model.user.User;

import java.util.Optional;

public interface AuthoritiesRepository extends CrudRepository<Authorities, String> {

    Optional<Authorities> findByUser(@Param("user") User user) throws DataAccessException;

}
