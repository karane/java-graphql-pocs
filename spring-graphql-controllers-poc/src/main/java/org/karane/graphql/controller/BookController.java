package org.karane.graphql.controller;

import graphql.schema.DataFetchingEnvironment;
import org.karane.graphql.model.Book;
import org.karane.graphql.model.BookFilter;
import org.karane.graphql.model.EnvironmentInfo;
import org.karane.graphql.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Locale;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<Book> books(@Argument BookFilter filter) {
        return bookService.findAll(filter);
    }

    @QueryMapping
    public EnvironmentInfo environment(DataFetchingEnvironment env) {
        String field = env.getField().getName();
        Locale locale = env.getLocale() != null ? env.getLocale() : Locale.getDefault();
        List<String> selectedFields = env.getSelectionSet().getFields().stream()
                .map(graphql.schema.SelectedField::getName)
                .toList();

        return new EnvironmentInfo(field, locale.toLanguageTag(), selectedFields);
    }
}
