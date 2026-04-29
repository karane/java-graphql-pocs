package org.karane.graphql.service;

import org.karane.graphql.model.Author;
import org.karane.graphql.model.Book;
import org.karane.graphql.model.Publisher;
import org.karane.graphql.model.Review;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookService {

    private final Map<String, Book> books = new LinkedHashMap<>();

    public BookService() {
        // Book 1: fully valid — all non-null fields set
        Author author1 = new Author("a1", "Robert C. Martin", null,
                new Publisher("p1", "Addison-Wesley", "US"));
        books.put("1", new Book("1", "Clean Code", author1, 464,
                new Review("r1", 5, "A timeless classic on software craftsmanship")));

        // Book 2: Review.comment is null -- triggers NonNullableFieldWasNullError.
        Author author2 = new Author("a2", "Jane Doe", "A prolific author", null);
        books.put("2", new Book("2", "Broken Review Book", author2, 300,
                new Review("r2", 4, null)));

        // Book 3: Publisher.name is null -- triggers NonNullableFieldWasNullError.
        Author author3 = new Author("a3", "John Smith", "A veteran author",
                new Publisher("p3", null, "UK"));
        books.put("3", new Book("3", "Broken Publisher Book", author3, 200,
                new Review("r3", 3, "Decent read")));
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public List<Book> searchByTitlePrefix(String prefix) {
        return books.values().stream()
                .filter(b -> b.title().startsWith(prefix))
                .toList();
    }
}
