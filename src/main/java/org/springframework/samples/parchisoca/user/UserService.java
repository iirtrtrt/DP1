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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import java.awt.Color;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class UserService{

    @Autowired
	private final UserRepository userRepository;



    @Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;

	}

    //used for saving new user and updating existing user
	@Transactional
	public void saveUser(User user, UserRole role) throws DataAccessException {
  		user.setEnabled(true);
		user.setRole(role);
		System.out.println("Saving user with role " + user.getRole());
        user.setCreatedTime(LocalDate.now());
        //this.emailService.sendRegistrationEmail(user.getEmail());
        userRepository.save(user);
	}

    @Transactional
	public void saveUser(User user) throws DataAccessException {
  		user.setEnabled(true);
        user.setCreatedTime(LocalDate.now());
        //this.emailService.sendRegistrationEmail(user.getEmail());
        userRepository.save(user);
	}


    public Optional<User> getCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("current user: " + currentPrincipalName);
        return findUser(currentPrincipalName);
    }



	public Optional<User> findUser(String username) {
		return userRepository.findById(username);
	}
    public List<User> findAllUsersWithEmail() {
        return userRepository.findByEmailNotNull();
    }

    public List<User> findAllUsers(){
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


}
