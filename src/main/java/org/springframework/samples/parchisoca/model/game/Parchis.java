package org.springframework.samples.parchisoca.model.game;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Parchis extends GameBoard {
    @Id
    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }
    private Integer repetitions;

    private Integer yellowFinished = 0;
    private Integer greenFinished = 0;
    private Integer redFinished = 0;
    private Integer blueFinished = 0;

    private boolean extraAction = true;
    private boolean kick = false;
}
