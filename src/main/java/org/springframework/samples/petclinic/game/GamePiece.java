package org.springframework.samples.petclinic.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.enums.GameStatus;
import org.springframework.samples.petclinic.enums.GameType;
import org.springframework.samples.petclinic.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.awt.*;

@Entity
@Getter
@Setter
public class GamePiece {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer gamePiece_id;

    @NotEmpty
    private Color tokenColor;


    //this should not be like this the game piece 
    //should not be connected to both Game and GameBoard
    /*@ManyToOne(optional = false)
    private Game game_id;*/

    @ManyToOne
    GameBoard board;

    /*@OneToOne(optional = false)
    private User user_id;*/

    int xPosition;
    int yPosition;


    public Integer getPositionXInPixels(Integer size) {
    	return (xPosition)*size;
    }
    
    public Integer getPositionYInPixels(Integer size) {
    	return (yPosition)*size;
    }
    
    
    
}
