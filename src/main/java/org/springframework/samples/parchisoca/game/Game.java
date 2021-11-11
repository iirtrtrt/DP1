package org.springframework.samples.parchisoca.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.parchisoca.enums.GameStatus;
import org.springframework.samples.parchisoca.enums.GameType;
import org.springframework.samples.parchisoca.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int game_id;

    @NotEmpty
    private String name;

    @ManyToOne()
    //@JoinColumn(name = "username")
    private User creator;

    private int max_player;

    @ManyToOne()
    // @JoinColumn(name = "won_games")
    private User winner;

    @OneToOne(mappedBy = "game")
    private GameBoard gameboard;

    @ManyToMany()
    @JoinTable(name = "game_user",
        joinColumns = { @JoinColumn(name = "fk_game") },
        inverseJoinColumns = { @JoinColumn(name = "fk_user") })
    private List<User> other_players;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;



    public void addUser(User user) throws Exception
    {
        if(other_players == null)
            other_players = new ArrayList<>();
       other_players.add(user);
    }

    public boolean checkColors(Color color)
    {
        List<User> all_players = this.getOther_players();
        all_players.add(this.getCreator());


        for(User user : all_players )
        {
            System.out.println("Color: " + color.toString());
            System.out.println("Color of creator: " + user.getGamePieces().get(0).getTokenColor().toString());
            if(user.getGamePieces().get(0).getTokenColor().getRGB() == color.getRGB())
           {
               System.out.println("color is the same");
               //System.out.println("User: " + user.getUsername() + " has color " + user.getGamePieces().get(0).getTokenColor().toString());
               return false;
           }
        }

        return true;
    }

    public boolean checkMaxAmountPlayers()
    {
        if(this.getOther_players() != null && this.getOther_players().size() != 0)
        {
            for(User user : this.getOther_players())
                System.out.println(user.getUsername());

        }
        return this.getOther_players().size() + 1 < max_player;
    }



}
