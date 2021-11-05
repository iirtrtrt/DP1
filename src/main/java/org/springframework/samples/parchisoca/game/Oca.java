package org.springframework.samples.parchisoca.game;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Oca extends GameBoard{
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
