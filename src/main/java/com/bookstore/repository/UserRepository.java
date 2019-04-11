package com.bookstore.repository;

import com.bookstore.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * created by saikat on 4/11/19
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
