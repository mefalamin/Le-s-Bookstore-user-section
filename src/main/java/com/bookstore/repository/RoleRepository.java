package com.bookstore.repository;

import com.bookstore.domain.security.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * created by saikat on 4/11/19
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByname(String name);


}
