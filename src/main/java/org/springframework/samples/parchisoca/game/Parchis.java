package org.springframework.samples.parchisoca.game;

import java.util.List;
import java.util.Map;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.parchisoca.game.GameBoard;
import org.springframework.samples.parchisoca.user.User;




@Getter
@Setter
@Entity
@Table(name = "parchis")
public class Parchis extends GameBoard{
    @Id
    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }


    

    

    Integer repetitions;

}
