package com.bookstore.service.impl;

import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * created by saikat on 4/11/19
 */

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private PasswordResetTokenRepository passwordResetTokenRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenforUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token,user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
         User localUser = userRepository.findByUsername(user.getUsername());

         if(localUser !=null){
             LOG.info("user {} already exists.Nothing will be done.",user.getUsername());

         }else
         {
             for (UserRole ur : userRoles
                  ) {
                 roleRepository.save(ur.getRole());
                 
             }
            user.getUserRoles().addAll(userRoles);
             localUser = userRepository.save(user);
         }
         return localUser;
    }
}
