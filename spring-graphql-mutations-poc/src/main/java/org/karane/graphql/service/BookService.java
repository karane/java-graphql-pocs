package org.karane.graphql.service;

import org.karane.graphql.model.Author;
import org.karane.graphql.model.Book;
import org.karane.graphql.model.CreateBookInput;
import org.karane.graphql.model.UpdateBookInput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookService {
    private final ConcurrentHashMap<String, Book> books = new ConcurrentHashMap<>();

    public BookService() {
        books.put("1", new Book("1", "Clean Code", new Author("1", "Robert C. Martin"), 464));
        books.put("2", new Book("2", "Refactoring", new Author("2", "Martin Fowler"), 418));
        books.put("3", new Book("3", "Domain-Driven Design", new Author("3", "Eric Evans"), 529));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Book findById(String id) {
        return books.values().stream()
                .filter(b -> b.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
    }

    public Book create(CreateBookInput input) {
        String id = UUID.randomUUID().toString();
        Author author = new Author(UUID.randomUUID().toString(), input.authorName());
        Book book = new Book(id, input.title(), author, input.pages());
        books.put(id, book);
        return book;
    }

    public Book update(String id, UpdateBookInput input) {
        Book existing = findById(id);
        String title = input.title() != null ? input.title() : existing.title();
        Author author = existing.author();
        if (input.authorName() != null) {
            author = new Author(author.id(), input.authorName());
        }
        Integer pages = input.pages() != null ? input.pages() : existing.pages();
        Book updated = new Book(id, title, author, pages);
        books.put(id, updated);
        return updated;
    }

    public boolean delete(String id) {
        findById(id);
        books.remove(id);
        return true;
    }
}
