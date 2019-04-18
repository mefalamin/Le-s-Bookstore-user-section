package com.bookstore.service.impl;

import com.bookstore.domain.UserPayment;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.service.UserPaymentService;
import org.springframework.stereotype.Service;

/**
 * created by saikat on 4/18/19
 */
@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    private UserPaymentRepository paymentRepository;

    public UserPaymentServiceImpl(UserPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public UserPayment findById(Long id) {
        return paymentRepository.findOne(id);
    }

    @Override
    public void removeById(Long id) {
            paymentRepository.delete(id);
    }
}
