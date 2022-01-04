package org.springframework.samples.parchisoca.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class StatisticUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "statistic")
    private User user;

    private int numberOfJoinedGames;
    private int numberOfWins;
    private int highscore;

    public StatisticUser(){}

    public StatisticUser(int numberOfPlayedGames, int numberOfWins, int highscore){
        this.numberOfJoinedGames = numberOfPlayedGames;
        this.numberOfWins = numberOfWins;
        this.highscore = highscore;
    }

    public void addGameToNumberOfJoinedGames(){
        System.out.println("In here");
        numberOfJoinedGames++;
        System.out.println("It worked");
    }


}
