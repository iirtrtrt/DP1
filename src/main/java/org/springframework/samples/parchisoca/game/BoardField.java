package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;

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
    GameBoard board;

    // @OneToMany
    //GamePiece piece;

    FieldType type;

    //TODO This Too
    String color;
    int xPosition_lu;
    int yPosition_lu;

    int xPosition_rb;
    int yPosition_rb;

    int number;

    @OneToOne
    BoardField next_field;


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
