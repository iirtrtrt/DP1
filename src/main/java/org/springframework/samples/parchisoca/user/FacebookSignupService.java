package org.springframework.samples.parchisoca.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.parchisoca.model.user.User;
import org.springframework.samples.parchisoca.repository.UserRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

@Service
public class FacebookSignupService implements ConnectionSignUp {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        user.setUsername(connection.getDisplayName());
        user.setPassword("FacebookUser");
        userRepository.save(user);
        return user.getUsername();
    }
}
