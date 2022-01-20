package org.springframework.samples.parchisoca.model.game;

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

    @Transient
    public static final int boardsize = 800;
    public static final int fieldSizeOca = 100;
    public static final int fieldSizeParchis = 40;

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
    List < BoardField > fields;

    @JoinColumn(name = "game")
    @OneToOne(cascade = CascadeType.ALL)
    Game game;

    @OneToMany
    public List<Option> options;
}
