package org.springframework.samples.parchisoca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.parchisoca.model.game.GamePiece;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePieceRepository extends JpaRepository<GamePiece, Integer> {


}
