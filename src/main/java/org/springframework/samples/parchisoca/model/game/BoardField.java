package org.springframework.samples.parchisoca.model.game;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.enums.FieldType;
import org.springframework.samples.parchisoca.model.BaseEntity;

import javax.persistence.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "boardFields")
public class BoardField extends BaseEntity {

    public static final int height =  80;



    @OneToMany
    private List<GamePiece> listGamesPiecesPerBoardField;

    @ManyToOne
    private GameBoard board;

    private FieldType type;

    private ActionType action;

    //TODO This Too
    private String color;
    private int xPosition_lu;
    private int yPosition_lu;

    private boolean parchis_special = false;

    private int xPosition_rb;
    private int yPosition_rb;

    private int number;

    @OneToOne
    public BoardField next_field;


    public BoardField(int number, String color, FieldType type, int x, int y, int x_, int y_) {
        this.number = number;
        this.color = color;
        this.type = type;
        this.xPosition_lu = x;
        this.yPosition_lu = y;
        this.xPosition_rb = x_;
        this.yPosition_rb = y_;
    }

    public BoardField() {

    }

    public BoardField(int number, String color, FieldType type, int x, int y, int x_, int y_, ActionType action) {
        this.number = number;
        this.color = color;
        this.type = type;
        this.xPosition_lu = x;
        this.yPosition_lu = y;
        this.xPosition_rb = x_;
        this.yPosition_rb = y_;
        this.action = action;
    }




    public Integer getPositionXluInPixels(Integer size) {
        return (xPosition_lu) * size;
    }

    public Integer getPositionYluInPixels(Integer size) {
        return (yPosition_lu) * size;
    }


    public Integer getPositionXrbInPixels(Integer size) {
        return (xPosition_rb) * size;
    }

    public Integer getPositionYrbInPixels(Integer size) {
        return (yPosition_rb) * size;
    }

}
