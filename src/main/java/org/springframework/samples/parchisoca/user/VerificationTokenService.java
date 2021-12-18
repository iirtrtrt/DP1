package org.springframework.samples.parchisoca.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class VerificationTokenService
{

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository)
    {
        this.verificationTokenRepository  = verificationTokenRepository;
    }

    public Optional<VerificationToken> findByToken(String token)
    {
        return verificationTokenRepository.findVerificationTokenByToken(token);
    }


    public void deleteVerificationToken(Integer id)
    {
        verificationTokenRepository.deleteById(id);
    }

    @Transactional
    public void save(VerificationToken token)
    {
        verificationTokenRepository.save(token);
    }
}
