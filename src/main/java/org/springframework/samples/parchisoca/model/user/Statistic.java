package org.springframework.samples.parchisoca.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic {

    private int numberOfPlayedGames;
    private int numberOfWins;
    private int numberOfrolledDices;
    private double winRate;
    private String username;

    public Statistic(int numberOfPlayedGames, int numberOfWins, int numberOfrolledDices, String username){
        this.numberOfPlayedGames = numberOfPlayedGames;
        this.numberOfWins = numberOfWins;
        this.numberOfrolledDices = numberOfrolledDices;
        if(numberOfPlayedGames == 0) {
            this.winRate = 0.0;
        } else {
            this.winRate = (double) numberOfWins/ (double) numberOfPlayedGames;
        }
        this.username = username;
    }


}
