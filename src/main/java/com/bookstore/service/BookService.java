package com.bookstore.service;

import com.bookstore.domain.Book;

import java.util.List;

/**
 * created by saikat on 4/13/19
 */
public interface BookService {

    List<Book> findAll();
    Book findOne(Long id);
}
