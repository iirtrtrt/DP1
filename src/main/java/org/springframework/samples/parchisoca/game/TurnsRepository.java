package org.springframework.samples.parchisoca.game;


import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.parchisoca.user.User;

import java.util.Optional;

public interface TurnsRepository extends CrudRepository<Turns, Integer>{



}
