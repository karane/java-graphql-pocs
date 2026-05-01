package org.karane.graphql.service;

import org.karane.graphql.model.Author;
import org.karane.graphql.model.Book;
import org.karane.graphql.model.BookFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final List<Book> books;

    public BookService() {
        Author fowler = new Author("1", "Martin Fowler");
        Author martin = new Author("2", "Robert C. Martin");

        books = new ArrayList<>();
        books.add(new Book("1", "Refactoring", fowler));
        books.add(new Book("2", "Patterns of Enterprise Application Architecture", fowler));
        books.add(new Book("3", "Clean Code", martin));
        books.add(new Book("4", "Clean Architecture", martin));
    }

    public List<Book> findAll(BookFilter filter) {
        return books.stream()
                .filter(b -> filter == null ||
                        (filter.titleContains() == null || b.title().toLowerCase().contains(filter.titleContains().toLowerCase())) &&
                        (filter.authorId() == null || b.author().id().equals(filter.authorId())))
                .toList();
    }
}
