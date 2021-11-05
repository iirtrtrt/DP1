package org.springframework.samples.parchisoca.game;

import org.springframework.samples.parchisoca.enums.FieldType;

import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OcaService {

    @Autowired 
	OcaRepository OcaRepo;

    // public static final String STANDARD_FILL_COLOR  = "#fef9e7" ;
    // public static final String GREEN_END  = "#26ca0c" ;
    // public static final String RED_END  = "#e32908" ;
    // public static final String BLUE_END  = "#0890e3" ;
    // public static final String YELLOW_END = "#dbe117";

    // public static final Integer FIELD_WIDTH  = 2;
    // public static final Integer FIELD_HEIGHT  = 1;

    // public Optional<Oca> findById(Integer id){
	// 	return OcaRepo.findById(id);
	// }

    //Calculates all the Board Field entities that are needed 
    public void createGameFields(List<BoardField> fields){
    }
}
