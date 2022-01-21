
package org.springframework.samples.parchisoca.model.game;

import lombok.Getter;
import lombok.Setter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.enums.TurnState;
import org.springframework.samples.parchisoca.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {

    @Transient
    private static final Logger logger = LogManager.getLogger(Game.class);


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int game_id;

    @NotEmpty
    private String name;

    @ManyToOne()
    //@JoinColumn(name = "username")
    private User creator;

    Integer dice;

    private int max_player;


    @ElementCollection
    private List<String> history_board;

    private boolean AI;

    @OneToOne
    private User current_player;

    private boolean has_started = false;

    private TurnState turn_state = TurnState.INIT;

    private Integer actionMessage;

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

    @ManyToMany
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

    private static final int MAX_HISTORYBOARD_SIZE = 8;

    public void addUser(User user) {
        logger.info("adding user " + user.getUsername() + " to game: " + this.getName());
        if (other_players == null)
            other_players = new ArrayList <> ();

        other_players.add(user);
        current_players.add(user);
        if (current_players.size() == max_player) {
            addToHistoryBoard("The Game has started!");
            has_started = true;
            status = GameStatus.ONGOING;
            logger.info("setting state to " + GameStatus.ONGOING);
        }
    }

    public void addTurn(Turns turn) {
        if (turns == null)
            turns = new ArrayList <> ();

        turns.add(turn);

    }

    public boolean checkMaxAmountPlayers() {
        logger.info(this.getCurrent_players());
        return this.getCurrent_players().size() < max_player;
    }

    public void rollDice() {
        Random rand = new Random();
        this.dice = rand.nextInt(6) + 1;
        if(this.getCurrent_player() != null){
            this.addToHistoryBoard(this.getCurrent_player().getFirstname() + ": Rolled a " + Integer.toString(this.dice));
        }
    }

    public Color getColorOfCurrentPlayer() {
      return this.getCurrent_player().getGamePieces().get(0).getTokenColor();
    }


    public void setCurrent_players(User user) {
        current_players = new ArrayList < > ();
        logger.info("Current players.size before" + current_players.size());
        current_players.add(user);
        logger.info("Current players.size" + current_players.size());
    }
    public void setTurns(Turns turn){
        turns = new ArrayList<>();
        turns.add(turn);
    }

  public BoardField getStartField()
  {
      return this.gameboard.fields.get(0);
  }


    public void addToHistoryBoard(String play) {
        if(history_board == null) history_board = new ArrayList<String>();

        history_board.add(play);
        if(history_board.size() > MAX_HISTORYBOARD_SIZE){
            history_board.remove(0);
        }
    }
}
