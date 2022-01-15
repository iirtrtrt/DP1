package org.springframework.samples.parchisoca.game;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.samples.parchisoca.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "options")
public class Option {

    public static final String MOVE_HOME =  "Move piece from home";
    public static final String REPEAT =     "Repeat Turn";
    public static final String PASS =       "Pass Turn";
    public static final String LOOSE =      "Loose Piece";
    public static final String MOVE_OCA =   "Move Piece";
    public static final String ORDER =      "Let's decide the order!!";
    public static final String MOVE =       "Move piece to field ";

    Option(Integer number, String text){
        this.text = text;
        this.number = number;
    }

    Option(){};



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    private String text;
    private Integer number;
    private Boolean choosen = false;
}


