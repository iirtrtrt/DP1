package org.springframework.samples.parchisoca.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.samples.parchisoca.model.user.Statistic;


public class StatisticTest {

    Statistic statistic;

    @Test
    void shouldReturnCorrectWinRate()
    {
        statistic = new Statistic(10,5,3,"username");
        Assertions.assertEquals(statistic.getWinRate(), 0.5);
    }

    @Test
    void shouldReturnCorrectWinRate0()
    {
        statistic = new Statistic(0,0,3,"username");
        Assertions.assertEquals(statistic.getWinRate(), 0.0);
    }
}
