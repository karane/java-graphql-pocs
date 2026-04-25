package org.karane.graphql.controller;

import org.karane.graphql.model.Author;
import org.karane.graphql.model.Book;
import org.karane.graphql.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public Book book(@Argument String id) {
        return bookService.findById(id);
    }

    @QueryMapping
    public List<Book> books() {
        return bookService.findAll();
    }

    @QueryMapping
    public List<Book> booksByAuthor(@Argument String authorId) {
        return bookService.findByAuthorId(authorId);
    }

    @MutationMapping
    public Book addBook(@Argument String title, @Argument String authorId) {
        return bookService.addBook(title, authorId);
    }

    @SchemaMapping(typeName = "Book", field = "author")
    public Author author(Book book) {
        return bookService.findAuthorById(book.authorId());
    }
}
