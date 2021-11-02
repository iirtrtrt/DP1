package org.springframework.samples.petclinic.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GamePiece;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @NotNull
    @Size(min=4, max=30)
    @Id
    String username;

    String firstname;

    String lastname;

    @Email
    String email;

    UserRole role = UserRole.USER;

    @NotNull
    @Size(min=4, max=30)
    String password;

    String passwordConfirm;

    boolean enabled = false;

    private Color tokenColor;

    @OneToOne
    GamePiece gamePiece;

    /**
    * maybe it would be smarter to only have 1 List of all games that combines played, created, and won games.
    */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "winner")
    private List<Game> won_games;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<Game> created_games;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "other_players")
    private Set<Game> played_games;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authorities> authorities;

    public void addCreatedGame(Game game) { played_games.add(game); }

    @Override
    public String toString() {
        System.out.println("hello here");
        return new ToStringCreator(this)
            .append("lastName", this.lastname)
            .append("firstName", this.firstname).append("username", this.username).append("password",this.password).append("passwordConfirm",this.passwordConfirm).toString();
    }


                    }
