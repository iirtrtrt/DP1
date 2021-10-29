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

    @NotEmpty
    private Color tokenColor;

    @ManyToOne(optional = false)
    private Game game_id;

    @OneToOne(optional = false)
    private User user_id;
    
}
