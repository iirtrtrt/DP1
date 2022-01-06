package org.springframework.samples.parchisoca.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class StatisticUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "statistic")
    private User user;

    private int numberOfPlayedGames;
    private int numberOfWins;
    private int highscore;

    public StatisticUser(){}

    public StatisticUser(int numberOfPlayedGames, int numberOfWins, int highscore){
        this.numberOfPlayedGames = numberOfPlayedGames;
        this.numberOfWins = numberOfWins;
        this.highscore = highscore;
    }

    public void addGameToNumberOfPlayedGames(){
        numberOfPlayedGames++;
    }


}
