package com.example.BookShop.controller;

import com.example.BookShop.model.Book;
import com.example.BookShop.repository.BookRepository;
import com.example.BookShop.response.MessageResponse;
import com.example.BookShop.service.ImageUploadingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private ImageUploadingService imageUploadingService;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/get-book/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") String id) {
            Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/add-book", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBook(@RequestParam("file") MultipartFile multipartFile) {
//        String title = request.getParameter("title");
//        String description = request.getParameter("description");
//        String author = request.getParameter("author");
//        Double price = Double.valueOf(request.getParameter("price"));
//        int amount = Integer.parseInt(request.getParameter("amount"));
//        String category = request.getParameter("category");
        UUID uuid = UUID.randomUUID();
            String image = imageUploadingService.upload(multipartFile, uuid.toString());
            if (Objects.equals(image, "Image couldn't upload, Something went wrong"))
                return ResponseEntity.badRequest().body(new MessageResponse
                        (HttpStatus.BAD_REQUEST.value(), "Something went wrong, please try again later"));

//            Book book = new Book(title, description, author, price, amount,
//                    image, category, 0, uuid.toString());
//
//            bookRepository.save(book);
            return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(), "Book added successfully"));
    }

    @DeleteMapping("/delete-book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") String id) {
        try{
            bookRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(),"The book has been deleted"));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-book")
    public ResponseEntity<?> updateBook(@Valid @RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        String id = request.getParameter("id");
        Optional<Book> bookData = bookRepository.findById(id);
        if (bookData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse(HttpStatus.UNAUTHORIZED.value(), "Book not found"));
        }
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String author = request.getParameter("author");
        Double price = Double.valueOf(request.getParameter("price"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String category = request.getParameter("category");

        String image = imageUploadingService.upload(multipartFile, id);
        if (Objects.equals(image, "Image couldn't upload, Something went wrong"))
            return ResponseEntity.badRequest().body(new MessageResponse
                    (HttpStatus.BAD_REQUEST.value(), "Something went wrong, please try again later"));

        Book _bookInfo = bookData.get();
        _bookInfo.setTitle(title);
        _bookInfo.setDescription(description);
        _bookInfo.setAuthor(author);
        _bookInfo.setPrice(price);
        _bookInfo.setAmount(amount);
        _bookInfo.setCategory(category);

        bookRepository.save(_bookInfo);
        return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(), "Book information updated successfully"));
    }
}
