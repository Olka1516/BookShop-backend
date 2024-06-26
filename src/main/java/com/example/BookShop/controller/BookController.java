package com.example.BookShop.controller;

import com.example.BookShop.model.Book;
import com.example.BookShop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/get-book/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") String id) {
            Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
