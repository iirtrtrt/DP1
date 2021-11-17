package org.springframework.samples.parchisoca.game;

import java.util.List;

import javax.persistence.*;

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

    @OneToMany
    List<Option> options;

}
