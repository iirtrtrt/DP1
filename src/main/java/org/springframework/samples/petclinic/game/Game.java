package org.springframework.samples.petclinic.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.enums.GameStatus;
import org.springframework.samples.petclinic.user.User;

import javax.persistence.*;
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

    @ManyToOne()
    //@JoinColumn(name = "username")
    private User creator;

    @ManyToOne()
   // @JoinColumn(name = "won_games")
    private User winner;

    @ManyToMany()
    @JoinTable(name = "game_user",
        joinColumns = { @JoinColumn(name = "fk_game") },
        inverseJoinColumns = { @JoinColumn(name = "fk_user") })
    private List<User> other_players;

    private GameStatus status = GameStatus.CREATED;

    private LocalDateTime startTime;

    private LocalDateTime endTime;




}
