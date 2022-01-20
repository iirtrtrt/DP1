package org.springframework.samples.parchisoca.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.parchisoca.model.user.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);

    String deleteByUsername(String username);

    List<User> findByEmailNotNull();

    List<User> findAll();
}
