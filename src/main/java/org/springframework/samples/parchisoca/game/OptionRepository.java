package org.springframework.samples.parchisoca.game;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OptionRepository extends CrudRepository<Option, Integer>{

    Optional<Option> findByText(@Param("text") String name) throws DataAccessException;

}
