package org.springframework.samples.parchisoca.user;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.parchisoca.game.Game;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EmailService.class}))
public class VerificationTokenServiceTests
{

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserService userService;

    @Test
    public void saveVerificationTokenAndFind()
    {
        Optional<User> optionalUser = this.userService.findUser("flogam1");
        if(optionalUser.isPresent())
        {
            VerificationToken token = new VerificationToken(optionalUser.get());
            this.verificationTokenService.save(token);


            Optional<VerificationToken> optionaltoken =  this.verificationTokenService.findByToken(token.getToken());
            Assertions.assertTrue(optionaltoken.isPresent());
            Assertions.assertEquals(optionaltoken.get().token, token.getToken());
        }

    }

    @Test
    public void findVerificationTokenThatDoesNotExist()
    {
        Assertions.assertFalse(this.verificationTokenService.findByToken("idontexist").isPresent());
    }
}
