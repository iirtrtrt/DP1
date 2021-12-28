package org.springframework.samples.parchisoca.user;

import antlr.Token;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.game.BoardField;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.samples.parchisoca.game.GamePiece;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashSet;
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
    private LocalDate createdTime;


    boolean enabled = false;

    private Color tokenColor;

    private Boolean myTurn = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_id")
    private List<GamePiece> gamePieces;

    // TODO maybe it would be smarter to only have 1 List of all games that combines played, created, and won games.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "winner")
    private List<Game> won_games;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<Game> created_games;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "other_players")
    private Set<Game> played_games;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authorities> authorities;

    @OneToMany(mappedBy = "user")
    private Set<VerificationToken> tokens;


    public void addCreatedGame(Game game) { created_games.add(game); }
    public void addJoinedGame(Game game) { 
        if(played_games == null){
            played_games = new HashSet<Game>();
        }
        played_games.add(game); }

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

    @Override
    public String toString() {
        System.out.println("hello here");
        return new ToStringCreator(this)
            .append("lastName", this.lastname)
            .append("firstName", this.firstname).append("username", this.username)
            .append("email", this.email).append("password",this.password).append("passwordConfirm",this.passwordConfirm).toString();
    }
    public void choosePlay() {
    }



}
