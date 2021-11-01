package org.springframework.samples.petclinic.game;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Positive;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Parchis extends GameBoard{
    String background;

    @Positive
    int width;
    @Positive
    int height;

   // @OneToMany(cascade = CascadeType.ALL,mappedBy = "board",fetch = FetchType.EAGER)
    //List<GamePiece> pieces; 

    //@OneToMany(cascade = CascadeType.ALL,mappedBy = "board")
    //List<BoardField> fields; 
    
}
