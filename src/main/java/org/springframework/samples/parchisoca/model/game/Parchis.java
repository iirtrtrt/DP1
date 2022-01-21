package org.springframework.samples.parchisoca.model.game;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "parchis")
public class Parchis extends GameBoard {
    @Id
    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }
    Integer repetitions;

    Integer yellowFinished = 0;
    Integer greenFinished = 0;
    Integer redFinished = 0;
    Integer blueFinished = 0;

    boolean extraAction = true;
    boolean kick = false;
}
