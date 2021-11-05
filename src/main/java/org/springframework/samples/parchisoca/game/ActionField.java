
package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.parchisoca.model.BaseEntity;
import org.springframework.samples.parchisoca.game.BoardField;
import org.springframework.samples.parchisoca.enums.ActionType;

import javax.persistence.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="actionFields")
public class ActionField extends BoardField{
    @OneToMany
    private List<GamePiece> listGamesPiecesPerBoardField;

    @ManyToOne
    GameBoard board;

    // @OneToMany
    //GamePiece piece;

    
    //BoardField nextField;

    ActionType actionType;

    public ActionField(int id, String color, int x, int y, int x_,int y_, ActionType type){
        this.id = id;
        this.color = color;
        this.xPosition_lu = x;
        this.yPosition_lu = y;
        this.xPosition_rb = x_;
        this.yPosition_rb = y_;
        this.actionType = type;
    }

    public ActionField() {

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