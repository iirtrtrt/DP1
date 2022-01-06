package org.springframework.samples.parchisoca.game;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.parchisoca.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class   OptionService {
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

    @Transactional
    public Optional<Option> findOptionByText(String text) throws DataAccessException {
        return optionRepo.findByText(text);
    }

    public Optional<Option> findOption(int id) {
        return optionRepo.findById(id);
    }
}
