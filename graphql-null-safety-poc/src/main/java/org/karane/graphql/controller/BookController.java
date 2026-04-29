package org.karane.graphql.controller;

import jakarta.validation.constraints.NotNull;
import org.karane.graphql.model.Book;
import org.karane.graphql.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
@Validated
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public Book book(@Argument String id) {
        return bookService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Book> books() {
        return bookService.findAll();
    }

    // titlePrefix is nullable in SDL (String), but @NotNull enforces it at the Java layer.
    // Passing null triggers ConstraintViolationException --> ValidationExceptionResolver --> GraphQL error.
    @QueryMapping
    public List<Book> searchBooks(@Argument @NotNull String titlePrefix) {
        return bookService.searchByTitlePrefix(titlePrefix);
    }
}
