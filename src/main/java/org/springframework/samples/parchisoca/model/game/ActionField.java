
package org.springframework.samples.parchisoca.model.game;

import lombok.Getter;
import lombok.Setter;


import org.springframework.samples.parchisoca.enums.ActionType;
import org.springframework.samples.parchisoca.enums.FieldType;

import javax.persistence.*;

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

}
