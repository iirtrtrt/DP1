package org.springframework.samples.parchisoca.game;

import java.util.List;

import javax.persistence.*;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.parchisoca.game.GameBoard;

@Entity
@Getter
@Setter
@Table(name = "oca")
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
