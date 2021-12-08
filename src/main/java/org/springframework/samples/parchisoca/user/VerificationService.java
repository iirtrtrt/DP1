package org.springframework.samples.parchisoca.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationService
{

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    public VerificationService(VerificationTokenRepository verificationTokenRepository)
    {
        this.verificationTokenRepository  = verificationTokenRepository;
    }
}
