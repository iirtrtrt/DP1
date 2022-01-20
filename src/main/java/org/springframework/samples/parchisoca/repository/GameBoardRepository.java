package org.springframework.samples.parchisoca.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.parchisoca.model.game.GameBoard;

public interface GameBoardRepository extends CrudRepository <GameBoard, Integer > {}
