package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.model.game.Option;
import org.springframework.samples.parchisoca.service.OptionService;
import org.springframework.samples.parchisoca.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.Assert.*;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class OptionServiceTest {

    @Autowired
    OptionService optionService;

    @Test
    void shouldSaveNewOption()
    {
        Option option = new Option(12, "thisIsAOption");
        this.optionService.saveOption(option);

        Optional<Option> optionalOption = this.optionService.findOptionByText("thisIsAOption");
        assertTrue(optionalOption.isPresent());
        assertEquals(optionalOption.get().getText(), "thisIsAOption");
        assertEquals((int) optionalOption.get().getNumber(), 12);
    }

    @Test
    void shouldNotFindNonExistingOption()
    {
        Optional<Option> optionalOption = this.optionService.findOptionByText("thisIsNotAOption");
        assertFalse(optionalOption.isPresent());
    }
}
