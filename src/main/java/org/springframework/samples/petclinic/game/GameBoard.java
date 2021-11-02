package org.springframework.samples.petclinic.game;

import javax.persistence.Entity;
import javax.validation.constraints.Positive;

import org.springframework.samples.petclinic.model.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GameBoard extends BaseEntity{
    String background;

    @Positive
    int width;
    @Positive
    int height;

    //@OneToMany(cascade = CascadeType.ALL,mappedBy = "board",fetch = FetchType.EAGER)
    //List<GamePiece> pieces; 

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "board")
    List<BoardField> fields;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "gameboard")
    Game game;

}
