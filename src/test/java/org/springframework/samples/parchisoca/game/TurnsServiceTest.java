package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.user.EmailService;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.samples.parchisoca.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.Assert.*;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class TurnsServiceTest {

    @Autowired
    TurnsService turnsService;

    @Autowired
    UserService userService;

    @Test
    void shouldSaveNewTurn()
    {
        Turns turn = new Turns();
        turn.setNumber(4);
        
        Optional<User> optionalUser = this.userService.findUser("flogam1");

        if(optionalUser.isEmpty())
            Assertions.fail("User does not exist ");

        User found_user = optionalUser.get();
        turn.setUser_id(found_user);
        this.turnsService.saveTurn(turn);

        Optional<Turns> optionalTurn = this.turnsService.findTurn(1);
        assertTrue(optionalTurn.isPresent());
        
        assertEquals((int) optionalTurn.get().getNumber(), 4);
        assertEquals((User) optionalTurn.get().getUser_id(), found_user);
    }

    @Test
    void shouldNotFindNonExistingTurn()
    {
        Optional<User> optionalUser = this.userService.findUser("flogam1");

        if(optionalUser.isEmpty())
            Assertions.fail("User does not exist ");

        
        Optional<Turns> optionalTurn = this.turnsService.findTurn(1);
        assertFalse(optionalTurn.isPresent());
    }
}
