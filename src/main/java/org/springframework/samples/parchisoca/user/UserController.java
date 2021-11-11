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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String VIEWS_OWNER_CREATE_FORM = "users/createOwnerForm";

	private final UserService userService;
	private final AuthoritiesService authoritiesService;

	@Autowired
	public UserController(UserService userService, AuthoritiesService authoritiesService) {
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


    @InitBinder
    public void initUserBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PasswordValidator());
    }


    @GetMapping(value = "/register")
	public String register(Map<String, Object> model) {
	    System.out.println("HELLO");
		User user = new User();
		model.put("user", user);
		return VIEWS_OWNER_CREATE_FORM;
	}


    @PostMapping(value = "/register")
	public String processCreationForm(@Valid User user, BindingResult result) {
		if (result.hasErrors()) {

            return VIEWS_OWNER_CREATE_FORM;
		}
		else {
			//creating user
            System.out.println("creating user " + user.getUsername());
            System.out.println("User " + user.getUsername());
            System.out.println("User password " + user.getPassword());
            //System.out.println("User firstname " + user.getFirstname());
            //System.out.println("User  lastname " + user.getLastname());


            if(userService.findUser(user.getUsername()).isPresent())
            {
                System.out.println("username already taken");
                result.rejectValue("username" , "duplicate","username already taken");
                return VIEWS_OWNER_CREATE_FORM;
            }
            //this.userService.setToken
            this.userService.saveUser(user);
            this.authoritiesService.saveAuthorities(user.getUsername(), "user");
			return "redirect:/";
		}
	}

}
