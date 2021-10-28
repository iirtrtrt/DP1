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
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer game_id;

    @NotEmpty
    private String name;

    private Color tokenColor;

    @ManyToOne()
    //@JoinColumn(name = "username")
    private User creator;

    private int max_player;


    @ManyToOne()
   // @JoinColumn(name = "won_games")
    private User winner;

    @ManyToMany()
    @JoinTable(name = "game_user",
        joinColumns = { @JoinColumn(name = "fk_game") },
        inverseJoinColumns = { @JoinColumn(name = "fk_user") })
    private List<User> other_players;

    private GameStatus status = GameStatus.CREATED;

    @Enumerated(EnumType.STRING)
    private GameType type;

    private LocalDateTime startTime;

    private LocalDateTime endTime;






}
