package org.springframework.samples.parchisoca.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionServiceTest {

    @Autowired
    OptionService optionService;

    @Test
    void shouldSaveNewOption()
    {
        Option option = new Option();
        option.setId(1234);
        option.setChoosen(true);
        option.setNumber(1);
        this.optionService.saveOption(option);

        Optional<Option> optionalOption = this.optionService.findOption(1234);
        assertTrue(optionalOption.isPresent());
        assertEquals(optionalOption.get().getId().toString(), 1234);
    }
}
