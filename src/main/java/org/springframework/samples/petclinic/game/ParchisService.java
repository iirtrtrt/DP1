package org.springframework.samples.petclinic.game;

import org.springframework.samples.petclinic.enums.FieldType;

import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParchisService {

    @Autowired 
	ParchisRepository parchisRepo;

    public static final String STANDARD_FILL_COLOR  = "#fef9e7" ;
    public static final String GREEN_END  = "#26ca0c" ;
    public static final String RED_END  = "#e32908" ;
    public static final String BLUE_END  = "#0890e3" ;
    public static final String YELLOW_END = "#dbe117";

    public static final Integer FIELD_WIDTH  = 2;
    public static final Integer FIELD_HEIGHT  = 1;

    public Optional<Parchis> findById(Integer id){
		return parchisRepo.findById(id);
	}

    //Calculates all the Board Field entities that are needed 
    public void createGameFields(List<BoardField> fields){
        int id;
        int column = 7; 
        int row = 0;

        // BoardField[][] field_array = new BoardField[20][20];  unfortunately this does not work with oneToMany relationship

        //create all base fields

        //ids 35 to 43 and 59 to 67
        id = 35;
        for(row = 0; row < 20; row++) {
            if(row == 9 || row == 10){
                id = 59;
                continue;
            }
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));          
            id++;
        }

        //fields 34 and 68
        column = 9;
        row = 0;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));    
        row = 19;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));          
        

        //ids 1-9 and 25-33
        column = 11;
        id = 33;
        for(row = 0; row < 20; row++) {
            if(row == 9 || row == 10){
                id = 9;
                continue;
            }
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_WIDTH, FIELD_HEIGHT ));          
            id--;
        }

        //ids 50 to 44 and 24 to 18
        row = 7;
        id = 50;
        for(column = 0; column < 20; column++) {
            if(column > 6 && column < 13){
                id = 24;
                continue;
            }
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));          
            id--;
        }

        //ids 52 to 58 and 10 to 16
        row = 11;
        id = 50;
        for(column = 0; column < 20; column++) {
            if(column > 6 && column < 13){
                id = 10;
                continue;
            }
            fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));          
            id--;
        }

        //ids 51 and 17
        column = 0;
        row = 9;
        id = 51;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));          
        column = 19;
        id = 17;
        fields.add(new BoardField(id, STANDARD_FILL_COLOR, column, row, FIELD_HEIGHT, FIELD_WIDTH ));  
        
        
        //create the end fields

        //green end fields 
        row =  9;
        id = 70; //Todo: not sure what ids for the end fields
        for(column = 1; column < 8; column++) {
            fields.add(new BoardField(id, GREEN_END, column, row, FIELD_HEIGHT, FIELD_WIDTH ));          
            id++;
        }


         //green end fields 
         row =  9;
         id = 90; //Todo: not sure what ids for the end fields
         for(column = 12; column < 19; column++) {
             fields.add(new BoardField(id, BLUE_END, column, row, FIELD_HEIGHT, FIELD_WIDTH ));          
             id++;
         }


         //ids red end fields
        column = 9; 
        id = 80;
        for(row = 1; row < 8; row++) {
            fields.add(new BoardField(id, RED_END, column, row, FIELD_WIDTH, FIELD_HEIGHT ));          
            id++;
        }
         

        //ids red end fields
        column = 9; 
        id = 100;
        for(row = 12; row < 19; row++) {
            fields.add(new BoardField(id, YELLOW_END, column, row, FIELD_WIDTH, FIELD_HEIGHT ));          
            id++;
        }
            
    }

    


    
}
