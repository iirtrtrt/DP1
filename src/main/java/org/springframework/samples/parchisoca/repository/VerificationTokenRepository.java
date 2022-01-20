package org.springframework.samples.parchisoca.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.parchisoca.model.user.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer>
{

    Optional<VerificationToken> findVerificationTokenByToken(String token);


}
