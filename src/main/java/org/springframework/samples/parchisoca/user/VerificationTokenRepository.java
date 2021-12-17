package org.springframework.samples.parchisoca.user;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer>
{

    Optional<VerificationToken> findVerificationTokenByToken(String token);


}
