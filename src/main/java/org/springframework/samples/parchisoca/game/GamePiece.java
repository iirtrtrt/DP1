package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;

import org.javatuples.Pair;
import org.springframework.samples.parchisoca.user.User;


import javax.persistence.*;

import java.awt.*;
import java.util.Map;

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

    //@ManyToOne
    //GameBoard board;


    @ManyToOne
    @JoinColumn(name = "field")
    BoardField field;

    @ManyToOne()
    @JoinColumn(name = "UNIVERSITY_ID")
    private User user_id;

   // int xPosition;
    //int yPosition;


    private static final Map<Color, Pair<Double, Double>> color_position_map = Map.of(
                Color.RED, Pair.with(3.5, 3.5),
                Color.BLUE, Pair.with(16.5, 3.5),
                Color.GREEN, Pair.with(3.5,16.5),
                Color.YELLOW, Pair.with(16.5, 16.5)
                );

    //Todo probably the work of Service??
    public Integer getPositionXInPixels(Integer size) {
        Double pos_percentage = 0.0;
        if(field == null){
            //piece is standing in base
            pos_percentage = color_position_map.get(this.tokenColor).getValue0();
        }
        else{
            //piece is standing on a game field
            //Calculates the middle of a board field
            pos_percentage = field.getPositionXluInPixels(size) + Double.valueOf(field.getPositionXrbInPixels(size))/2 ;
        }

    	return (int) Math.round((pos_percentage));
    }

    public Integer getPositionYInPixels(Integer size) {
        Double pos_percentage = 0.0;
        if(field == null){
            //piece is standing in base
            pos_percentage = color_position_map.get(this.tokenColor).getValue1();
        }
        else{
            //piece is standing on a game field
            //Calculates the middle of a board field
            pos_percentage = field.getPositionYluInPixels(size) + Double.valueOf(field.getPositionYrbInPixels(size))/2 ;
        }

    	return (int) Math.round(pos_percentage);
    }

    //Returns the tokenColor in String-Hex Format
    public String getColorInHex() {
        return String.format("#%06x", Integer.valueOf(this.tokenColor.getRGB() & 0x00FFFFFF));
    }




}
