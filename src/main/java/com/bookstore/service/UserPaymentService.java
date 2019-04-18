package com.bookstore.service;

import com.bookstore.domain.UserPayment;

/**
 * created by saikat on 4/18/19
 */
public interface UserPaymentService {


   UserPayment findById(Long id);

    void removeById(Long creditCardId);
}
