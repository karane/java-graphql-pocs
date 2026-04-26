package org.karane.graphql.controller;

import org.karane.graphql.model.Book;
import org.karane.graphql.model.CreateBookInput;
import org.karane.graphql.model.UpdateBookInput;
import org.karane.graphql.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@Validated
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<Book> books() {
        return bookService.findAll();
    }

    @QueryMapping
    public Book book(@Argument String id) {
        return bookService.findById(id);
    }

    @MutationMapping
    public Book createBook(@Argument @Valid CreateBookInput input) {
        return bookService.create(input);
    }

    @MutationMapping
    public Book updateBook(@Argument String id, @Argument @Valid UpdateBookInput input) {
        return bookService.update(id, input);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument String id) {
        return bookService.delete(id);
    }
}
