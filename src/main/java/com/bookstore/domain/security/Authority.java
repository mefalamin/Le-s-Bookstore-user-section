package com.bookstore.domain.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * created by saikat on 4/10/19
 */
public class Authority implements GrantedAuthority {
    private final String authority;

    public Authority (String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
