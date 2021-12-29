package org.springframework.samples.parchisoca.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticUserService {

    @Autowired
    private final StatisticUserRepository statisticUserRepository;

    @Autowired
    public StatisticUserService(StatisticUserRepository statisticUserRepository) {
        this.statisticUserRepository = statisticUserRepository;
    }


}
