package com.bookstore.service.impl;

import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.*;
import com.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private UserShippingRepository userShippingRepository;
    private UserPaymentRepository userPaymentRepository;

    public UserServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, RoleRepository roleRepository, UserShippingRepository userShippingRepository, UserPaymentRepository userPaymentRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userShippingRepository = userShippingRepository;
        this.userPaymentRepository = userPaymentRepository;
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

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {

        userPayment.setUser(user);
        userPayment.setUserBilling(userBilling);
        userPayment.setDefaultPayment(true);
        userBilling.setUserPayment(userPayment);
        user.getUserPaymentList().add(userPayment);
        save(user);
    }


    @Override
    public void setUserDefaultPayment(Long userPaymentId, User user) {

        List<UserPayment> userPaymentList =(List<UserPayment>) userPaymentRepository.findAll();

        for (UserPayment userPayment : userPaymentList)
        {
            if(userPayment.getId() == userPaymentId){
                userPayment.setDefaultPayment(true);
                userPaymentRepository.save(userPayment);
            }
            else {
                userPayment.setDefaultPayment(false);
                userPaymentRepository.save(userPayment);
            }
        }
    }


    @Override
    public void setUserDefaultShipping(Long shippingAddressId, User user) {

        List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();

        for(UserShipping userShipping : userShippingList){
            if(userShipping.getId() == shippingAddressId){
                userShipping.setUserShippingDefault(true);
                userShippingRepository.save(userShipping);

            }
            else {
                userShipping.setUserShippingDefault(false);
                userShippingRepository.save(userShipping);
            }
        }
    }

    @Override
    public void updateUserShipping(UserShipping userShipping, User user) {
        userShipping.setUser(user);
        userShipping.setUserShippingDefault(true);
        user.getUserShippingList().add(userShipping);
        save(user);
    }
}
