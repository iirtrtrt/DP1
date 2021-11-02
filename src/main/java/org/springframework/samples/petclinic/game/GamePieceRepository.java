package org.springframework.samples.petclinic.game;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.enums.GameStatus;

import java.util.List;

public interface GamePieceRepository extends CrudRepository<GamePiece, Integer> {


}
