package org.springframework.samples.parchisoca.game;

import javax.persistence.*;
import javax.validation.constraints.Positive;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GameBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }
    String background;

    @Positive
    int width;
    @Positive
    int height;



    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "board")
    List<BoardField> fields;

    @JoinColumn(name="game")
    @OneToOne(cascade = CascadeType.ALL)
    Game game;

}
