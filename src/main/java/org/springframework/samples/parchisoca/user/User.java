package org.springframework.samples.parchisoca.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.game.BoardField;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.GamePiece;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

    @NotNull
    @Size(min=4, max=30)
    @Id
    String username;

    String firstname;

    Integer pieces_finished = 0;

    String lastname;

    String email;

    UserRole role = UserRole.PLAYER;

    @NotNull
    @Size(min=4, max=30)
    String password;

    String passwordConfirm;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime createTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistic_id", referencedColumnName = "id")
    private StatisticUser statistic = new StatisticUser(0,0,0);

    boolean enabled = false;

    private Boolean locked = false;

    private Color tokenColor;

    private Boolean myTurn = false;

    private Integer stunTurns;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_id")
    private List<GamePiece> gamePieces = new ArrayList<>();

    // TODO maybe it would be smarter to only have 1 List of all games that combines played, created, and won games.

    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "played")
    // private List<Game> all_played_games;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "winner")
    private List<Game> won_games;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<Game> created_games;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "other_players")
    private Set<Game> played_games;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authorities> authorities;


    public void addCreatedGame(Game game) {
        created_games.add(game);
        statistic.addGameToNumberOfPlayedGames();
    }
    public void addJoinedGame(Game game) {
        played_games.add(game);
        statistic.addGameToNumberOfPlayedGames();
    }

    public boolean checkAlreadyCreatedGames()
    {
        for(Game game : created_games)
        {
            if(game.getStatus() == GameStatus.CREATED)
                return true;
        }
        return false;
    }

    public void setStartField(BoardField field)
    {
        this.getGamePieces().get(0).setField(field);
    }

    public void deleteAllGamePieces()
    {
       for(GamePiece gamePiece : gamePieces)
       {
           gamePiece.setUser_id(null);
           gamePiece.setTokenColor(null);
           gamePiece.setField(null);
       }
       gamePieces.clear();
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("lastName", this.lastname)
            .append("firstName", this.firstname)
            .append("username", this.username)
            .append("email", this.email)
            .append("password",this.password)
            .append("passwordConfirm",this.passwordConfirm)
            .append("createTime",this.createTime).toString();
    }
    public void choosePlay() {
    }




}
