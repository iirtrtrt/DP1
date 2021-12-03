package org.springframework.samples.parchisoca.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends  CrudRepository<User, String>{

    User findByUsername(String username);

    List<User> findByEmailNotNull();

}
