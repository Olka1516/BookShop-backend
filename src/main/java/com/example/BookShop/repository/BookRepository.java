package com.example.BookShop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.BookShop.model.Book;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findById(String id);
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :param, '%'))" +
            " OR LOWER(b.author) LIKE LOWER(CONCAT('%', :param, '%'))" +
            " OR LOWER(b.category) LIKE LOWER(CONCAT('%', :param, '%'))")
    List<Book> searchBooksByParam(@Param("param") String param);
}