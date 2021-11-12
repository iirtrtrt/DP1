package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.parchisoca.model.BaseEntity;

import javax.persistence.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="boardFields")
public class BoardField extends BaseEntity{


    @OneToMany
    private List<GamePiece> listGamesPiecesPerBoardField;

    @ManyToOne
    GameBoard board;

   // @OneToMany
    //GamePiece piece;

    //TODO Change to enum FieldType but for now it did not work with sqp
    String type;

    //TODO This Too
    String color;
    int xPosition_lu;
    int yPosition_lu;

    int xPosition_rb;
    int yPosition_rb;

    int number;

    //BoardField nextField;

    public BoardField(int number, String color, int x, int y, int x_,int y_){
        this.number = number;
        this.color = color;
        this.xPosition_lu = x;
        this.yPosition_lu = y;
        this.xPosition_rb = x_;
        this.yPosition_rb = y_;
    }

    public BoardField() {

    }


    public Integer getPositionXluInPixels(Integer size) {
    	return (xPosition_lu)*size;
    }

    public Integer getPositionYluInPixels(Integer size) {
    	return (yPosition_lu)*size;
    }


    public Integer getPositionXrbInPixels(Integer size) {
    	return (xPosition_rb)*size;
    }

    public Integer getPositionYrbInPixels(Integer size) {
    	return (yPosition_rb)*size;
    }


}
