
package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;



import org.springframework.samples.parchisoca.model.BaseEntity;
import org.springframework.samples.parchisoca.game.BoardField;
import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.enums.FieldType;

import javax.persistence.*;

import java.util.List;
import java.util.HashMap;

@Getter
@Setter
@Entity
@Table(name="actionFields")
public class ActionField extends BoardField{
    
    ActionType action;

    public ActionField(int number, String color, FieldType type, int x, int y, int x_, int y_, ActionType aType) {
        super(number, color, type, x, y, x_, y_);
        this.action = aType;
    }

    public ActionField() {

    }

    //public HashMap<ActionType,String> actionFieldInfoMap = new HashMap<ActionType,String>();

    // public Boolean showActionFieldInfo(ActionType actionType){
    //     switch(actionType){
    //         case GOOSE:
    //             System.out.println("GOOSE, falling in this action field will move you instantly to the next oca field, and you get a reroll.");
    //             break;
    //         case BRIDGE:
    //             System.out.println("BRIDGE, falling in this action field will move you instantly to the other bridge field, and you get a reroll.");
    //             break;
    //         case INN:
    //             System.out.println("INN, falling in this action field will make you loose 1 turn.");
    //             break;
    //         case DICE:
    //             System.out.println("DICE, falling in this action field will move you instantly to the other dice field, and you get a reroll.");
    //             break;
    //         case WELL:
    //             System.out.println("WELL, falling in this action field will make you loose 2 turns.");
    //             break;
    //         case MAZE:
    //             System.out.println("MAZE, falling in this action field will make you loose 3 turns.");
    //             break;
    //         case JAIL:
    //             System.out.println("JAIL, falling in this action field will make you loose 4 turns.");
    //             break;
    //         case DEATH:
    //             System.out.println("DEATH, falling in this action field will make you restart your game, moving your piece to the starting field.");
    //             break;
    //     }
    //     return false;
    // }
}