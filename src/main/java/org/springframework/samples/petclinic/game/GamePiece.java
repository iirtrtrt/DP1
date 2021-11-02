package org.springframework.samples.petclinic.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.enums.GameStatus;
import org.springframework.samples.petclinic.enums.GameType;
import org.springframework.samples.petclinic.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="gamePieces")
public class GamePiece {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer gamePiece_id;

    //should add some constraint here
    private Color tokenColor;


    //this should not be like this the game piece
    //should not be connected to both Game and GameBoard
    @ManyToOne()
    private Game game_id;

    @ManyToOne
    GameBoard board;

    @ManyToOne()
    private User user_id;

    int xPosition;
    int yPosition;


    public Integer getPositionXInPixels(Integer size) {
    	return (xPosition)*size;
    }

    public Integer getPositionYInPixels(Integer size) {
    	return (yPosition)*size;
    }



}