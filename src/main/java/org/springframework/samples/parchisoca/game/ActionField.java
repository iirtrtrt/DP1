
package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.parchisoca.model.BaseEntity;
import org.springframework.samples.parchisoca.game.BoardField;
import org.springframework.samples.parchisoca.enums.ActionType;

import javax.persistence.*;

import java.util.List;
import java.util.HashMap;

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

    public HashMap<ActionType,String> actionFieldInfoMap = new HashMap<ActionType,String>();

    public Boolean showActionFieldInfo(ActionType actionType){
        switch(actionType){
            case OCA:
                System.out.println("OCA, falling in this action field will move you instantly to the next oca field, and you get a reroll.");
                break;
            case BRIDGE:
                System.out.println("BRIDGE, falling in this action field will move you instantly to the other bridge field, and you get a reroll.");
                break;
            case INN:
                System.out.println("INN, falling in this action field will make you loose 1 turn.");
                break;
            case DICE:
                System.out.println("DICE, falling in this action field will move you instantly to the other dice field, and you get a reroll.");
                break;
            case WELL:
                System.out.println("WELL, falling in this action field will make you loose 2 turns.");
                break;
            case MAZE:
                System.out.println("MAZE, falling in this action field will make you loose 3 turns.");
                break;
            case JAIL:
                System.out.println("JAIL, falling in this action field will make you loose 4 turns.");
                break;
            case DEATH:
                System.out.println("DEATH, falling in this action field will make you restart your game, moving your piece to the starting field.");
                break;
        }
        return false;
    }
}