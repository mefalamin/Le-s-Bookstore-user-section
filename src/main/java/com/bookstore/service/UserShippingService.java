package com.bookstore.service;

import com.bookstore.domain.UserShipping;

/**
 * created by saikat on 4/19/19
 */

public interface UserShippingService {

    UserShipping findById(Long id);

    void removeById(Long shippingId);
}
