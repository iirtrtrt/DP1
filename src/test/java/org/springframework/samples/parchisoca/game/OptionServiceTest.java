package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class OptionServiceTest {

    @Autowired
    OptionService optionService;

    @Test
    void shouldSaveNewOption()
    {
        Option option = new Option();
        option.setNumber(12);
        option.setText("thisIsAOption");
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
