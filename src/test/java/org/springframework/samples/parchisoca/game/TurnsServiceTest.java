package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.user.EmailService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.Assert.*;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class TurnsServiceTest {

    @Autowired
    TurnsService turnsService;

    @Test
    void shouldSaveNewTurn()
    {
        Turns turn = new Turns();
        turn.setNumber(4);
        turn.setUsername("alec");
        this.turnsService.saveTurn(turn);

        Optional<Turns> optionalTurn = this.turnsService.findTurnByUsername("alec");
        assertTrue(optionalTurn.isPresent());
        assertEquals(optionalTurn.get().getUsername(), "alec");
        assertEquals((int) optionalTurn.get().getNumber(), 4);
    }

    @Test
    void shouldNotFindNonExistingTurn()
    {
        Optional<Turns> optionalTurn = this.turnsService.findTurnByUsername("alec");
        assertFalse(optionalTurn.isPresent());
    }
}
