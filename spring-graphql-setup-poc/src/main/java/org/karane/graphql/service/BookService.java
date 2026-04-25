package org.karane.graphql.service;

import org.karane.graphql.model.Author;
import org.karane.graphql.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BookService {

    private final Map<String, Author> authors = new LinkedHashMap<>();
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final AtomicInteger idSeq = new AtomicInteger(100);

    public BookService() {
        authors.put("1", new Author("1", "Martin Fowler"));
        authors.put("2", new Author("2", "Robert C. Martin"));
        books.put("1", new Book("1", "Refactoring", "1"));
        books.put("2", new Book("2", "Patterns of Enterprise Application Architecture", "1"));
        books.put("3", new Book("3", "Clean Code", "2"));
        books.put("4", new Book("4", "Clean Architecture", "2"));
    }

    public Book findById(String id) {
        return books.get(id);
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public List<Book> findByAuthorId(String authorId) {
        return books.values().stream()
                .filter(b -> b.authorId().equals(authorId))
                .toList();
    }

    public Book addBook(String title, String authorId) {
        if (!authors.containsKey(authorId)) {
            throw new IllegalArgumentException("Author not found: " + authorId);
        }
        String id = String.valueOf(idSeq.incrementAndGet());
        Book book = new Book(id, title, authorId);
        books.put(id, book);
        return book;
    }

    public Author findAuthorById(String id) {
        return authors.get(id);
    }
}
