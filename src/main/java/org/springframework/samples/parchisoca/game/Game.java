
package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.user.User;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {

    @Transient
    private static final Logger logger = LogManager.getLogger(Game.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int game_id;

    @NotEmpty
    private String name;

    @ManyToOne()
    //@JoinColumn(name = "username")
    private User creator;

    Integer dice;

    private int max_player;

    @OneToOne
    private User current_player;

    private boolean has_started = false;

    private TurnState turn_state = TurnState.INIT;


    /*@OneToMany
    Map<User,Integer> valuesPerPlayer;*/

    @ManyToOne()
    private User winner;

    @OneToOne(mappedBy = "game")
    private GameBoard gameboard;

    @ManyToMany()
    @JoinTable(name = "game_user",
        joinColumns = { @JoinColumn(name = "fk_game")},
        inverseJoinColumns = {
            @JoinColumn(name = "fk_user")
        })
    private List < User > other_players;

    @OneToMany
    private List < User > current_players;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turns> turns;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    public void addUser(User user) throws Exception {
        if (other_players == null)
            other_players = new ArrayList <> ();

        other_players.add(user);
        current_players.add(user);
        if (current_players.size() == max_player) {
            has_started = true;
            status = GameStatus.ONGOING;
            logger.info("setting state to " + GameStatus.ONGOING);
        }
    }

    public void addTurn(Turns turn) throws Exception {
        if (turns == null)
            turns = new ArrayList <> ();

        turns.add(turn);
        
    }

    public boolean checks(Color color) {

        for (User user: current_players) {
            if (user.getGamePieces().get(0).getTokenColor().getRGB() == color.getRGB())
                return false;
        }

        return true;
    }

    public boolean checkMaxAmountPlayers() {
        return this.getCurrent_players().size() < max_player;
    }

    @Transient
    public int getNumberPlayers() {
        if (other_players != null)
            return other_players.size();
        return 0;
    }

    public void rollDice() {
        Random rand = new Random();
        this.dice = rand.nextInt(6) + 1;
    }


    public Integer getAndResetDice() {
        Integer dice_roll = this.dice;
        dice = 0;
        return dice_roll;

    }

    public void setCurrent_players(User user) {
        current_players = new ArrayList < > ();
        current_players.add(user);
    }
    public void setTurns(Turns turn){
        turns = new ArrayList<>();
        turns.add(turn);
    }

  public BoardField getStartField()
  {
      return this.gameboard.fields.get(0);
  }
    //can be deleted
    public Integer getDice() {
        logger.info("Dice number: " + dice);
        return dice;
    }
}
