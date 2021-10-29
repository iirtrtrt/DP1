package org.springframework.samples.petclinic.game;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameBoardService {

	@Autowired 
	GameBoardRepository boardRepo;
	
	public Optional<GameBoard> findById(Integer id){
		return boardRepo.findById(id);
	}
}
