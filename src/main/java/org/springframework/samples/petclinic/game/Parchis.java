package org.springframework.samples.petclinic.game;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Positive;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Parchis extends GameBoard{
    @Id
    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }



    // @OneToMany(cascade = CascadeType.ALL,mappedBy = "board",fetch = FetchType.EAGER)
    //List<GamePiece> pieces;

    //@OneToMany(cascade = CascadeType.ALL,mappedBy = "board")
    //List<BoardField> fields;

}
