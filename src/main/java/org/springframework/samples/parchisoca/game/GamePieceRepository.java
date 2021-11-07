package org.springframework.samples.parchisoca.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePieceRepository extends JpaRepository<GamePiece, Integer> {


}
