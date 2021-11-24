package org.springframework.samples.parchisoca.game;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class OptionService {
    @Autowired
    OptionRepository optionRepo;

    @Autowired
    public OptionService(OptionRepository optionrepo) {
        this.optionRepo = optionrepo;
    }

    @Transactional
    public void saveOption(Option option) throws DataAccessException {
        optionRepo.save(option);
    }
}
