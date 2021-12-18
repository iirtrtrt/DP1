package org.springframework.samples.parchisoca.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository < User, String > {
    String deleteByUsername(String username);

    List < User > findByEmailNotNull();

    List < User > findAll();
}
