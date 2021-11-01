package org.springframework.samples.petclinic.game;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.petclinic.enums.FieldType;
import org.springframework.samples.petclinic.enums.GameStatus;
import org.springframework.samples.petclinic.enums.GameType;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="boardFields")
public class BoardField extends BaseEntity{


    @OneToMany
    private List<GamePiece> listGamesPiecesPerBoardField;

    @ManyToOne
    @JoinColumn(name = "board")
    GameBoard board;

    //TODO Change to enum FieldType but for now it did not work with sqp
    String type;

    //TODO This Too 
    String color;
    int xPosition_lu;
    int yPosition_lu;

    int xPosition_rb;
    int yPosition_rb;

    //BoardField nextField;

    public BoardField(int id, String color, int x, int y, int x_,int y_){
        this.id = id;
        this.color = color;
        this.xPosition_lu = x;
        this.yPosition_lu = y;
        this.xPosition_rb = x_;
        this.yPosition_rb = y_;
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
