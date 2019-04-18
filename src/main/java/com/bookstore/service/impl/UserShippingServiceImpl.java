package com.bookstore.service.impl;

import com.bookstore.domain.UserShipping;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserShippingService;
import org.springframework.stereotype.Service;

/**
 * created by saikat on 4/19/19
 */
@Service
public class UserShippingServiceImpl implements UserShippingService {

    private UserShippingRepository userShippingRepository;

    public UserShippingServiceImpl(UserShippingRepository userShippingRepository) {
        this.userShippingRepository = userShippingRepository;
    }

    @Override
    public UserShipping findById(Long id) {
        return userShippingRepository.findOne(id);
    }

    @Override
    public void removeById(Long shippingId) {
            userShippingRepository.delete(shippingId);
    }
}
