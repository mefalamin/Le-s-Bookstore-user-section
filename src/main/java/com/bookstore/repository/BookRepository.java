package com.bookstore.repository;

import com.bookstore.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * created by saikat on 4/13/19
 */
public interface BookRepository extends CrudRepository<Book,Long> {

     List<Book> findAll();
}
