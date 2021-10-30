package org.springframework.samples.petclinic.game;

import javax.persistence.Entity;
import javax.validation.constraints.Positive;

import org.springframework.samples.petclinic.model.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GameBoard extends BaseEntity{
    String background;

    @Positive
    int width;
    @Positive
    int height;

    public GameBoard(){
        this.background = "resources/images/board_parchis.png";
        this.width= 800;
        this.height = 800;
    }

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "board",fetch = FetchType.EAGER)
    List<GamePiece> pieces; 

    
}
