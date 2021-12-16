package org.springframework.samples.parchisoca.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends  CrudRepository<User, String>{

    Optional<User> findByUsername(String username);

    List<User> findByEmailNotNull();

    List<User> findAll();

}
