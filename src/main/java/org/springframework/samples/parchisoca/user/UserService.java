/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.parchisoca.user;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import java.awt.Color;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenService verificationTokenService) {
        this.userRepository = userRepository;
        this.verificationTokenService = verificationTokenService;
    }

    //used for saving new user and updating existing user
	@Transactional
	public void saveUser(User user, UserRole role) throws DataAccessException {
		user.setRole(role);

        if(!findUser(user.username).isPresent()) {
            user.setCreateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }
        userRepository.save(user);
    }

    @Transactional
    public void saveUser(User user) throws DataAccessException {
        if(user.getRole() != UserRole.ADMIN){
            user.setRole(UserRole.PLAYER);
        }
        if(!findUser(user.username).isPresent()) {
            user.setCreateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }
        userRepository.save(user);
    }

    void confirmUser(VerificationToken confirmationToken) {
        final User user = confirmationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        verificationTokenService.deleteVerificationToken(confirmationToken.getId());
    }

    public Optional < User > getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("current user: " + currentPrincipalName);
        return findUser(currentPrincipalName);
    }

    public Optional < User > findUser(String username) {
        return userRepository.findById(username);
    }

    public List < User > findAllUsersWithEmail() {
        return userRepository.findByEmailNotNull();
    }

    public boolean checkIfUserEmailAlreadyExists(String email){
        List <User> usersWithEmail = this.findAllUsersWithEmail();

        for(User u : usersWithEmail){
            if(u.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }

    public List < User > findAllUsers() {
        return userRepository.findAll();
    }

    public void setAI(User ai, User user){
        System.out.println("in setAI");
        String username = getRandomeAIString();
        while(findUser(username).isPresent()){
            username = getRandomeAIString();
        }
    
        ai.setUsername(username);
        ai.setFirstname("AI");
        ai.setLastname("");
        ai.setPassword("AIAIAI");
        ai.setRole(UserRole.AI);

        Color AITokenColor = user.getTokenColor() == Color.RED ? Color.YELLOW : Color.RED ;
        System.out.println("Before setTokenColor" + ai);

        ai.setTokenColor(AITokenColor);
        System.out.println("After setTokenColor" + ai);
        saveUser(ai, UserRole.AI);
    }

    public String getRandomeAIString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

        return generatedString;

    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public User getSelectedUser(String username) {
        return userRepository.findByUsername(username);
    }
}
