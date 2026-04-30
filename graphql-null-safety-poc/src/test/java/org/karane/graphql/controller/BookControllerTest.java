package org.karane.graphql.controller;

import org.junit.jupiter.api.Test;
import org.karane.graphql.exception.ValidationExceptionResolver;
import org.karane.graphql.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@GraphQlTest(BookController.class)
@Import({BookService.class, ValidationExceptionResolver.class})
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldResolveCleanBookWithAllNonNullFields() {
        graphQlTester.document("""
                {
                  book(id: "1") {
                    id
                    title
                    author { id name bio publisher { id name country } }
                    pages
                    review { id rating comment }
                  }
                }
                """)
                .execute()
                .path("book.title").entity(String.class).isEqualTo("Clean Code")
                .path("book.author.name").entity(String.class).isEqualTo("Robert C. Martin")
                .path("book.review.rating").entity(Integer.class).isEqualTo(5);
    }

    @Test
    void shouldReturnNullForNullableAuthorBio() {
        // bio: String (nullable in SDL) -- returning null from Java is valid, produces no error
        graphQlTester.document("""
                {
                  book(id: "1") {
                    author { bio }
                  }
                }
                """)
                .execute()
                .path("book.author.bio")
                .valueIsNull();
    }

    @Test
    void shouldReportErrorWhenNonNullReviewCommentIsNull() {
        // Review.comment is String! but Java holds null for book "2".
        // Expected error path: book --> review --> comment
        graphQlTester.document("""
                {
                  book(id: "2") {
                    id
                    title
                    review { rating comment }
                  }
                }
                """)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getPath()).contains("comment");
                });
    }

    @Test
    void shouldNullifyBookWhenReviewBubblesNull() {
        graphQlTester.document("""
                {
                  book(id: "2") {
                    id
                    title
                    review { rating comment }
                  }
                }
                """)
                .execute()
                .errors()
                .filter(e -> e.getPath().contains("comment"))
                .verify()
                .path("book")
                .valueIsNull();
    }

    @Test
    void shouldReportErrorWhenNonNullPublisherNameIsNull() {
        graphQlTester.document("""
                {
                  book(id: "3") {
                    id
                    author { name publisher { name country } }
                  }
                }
                """)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getPath()).contains("name");
                    assertThat(errors.get(0).getPath()).contains("publisher");
                });
    }

    @Test
    void shouldNullifyPublisherButKeepBookAndAuthorWhenPublisherNameBubblesNull() {
        graphQlTester.document("""
                {
                  book(id: "3") {
                    id
                    title
                    author { name publisher { name country } }
                  }
                }
                """)
                .execute()
                .errors()
                .filter(e -> e.getPath().contains("publisher"))
                .verify()
                .path("book.title").entity(String.class).isEqualTo("Broken Publisher Book")
                .path("book.author.name").entity(String.class).isEqualTo("John Smith")
                .path("book.author.publisher").valueIsNull();
    }

    @Test
    void shouldNullifyEntireDataWhenNonNullListItemBubblesNull() {
        graphQlTester.document("""
                {
                  books {
                    id
                    title
                    review { rating comment }
                  }
                }
                """)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getPath()).contains("comment");
                });
    }

    @Test
    void shouldRejectNullTitlePrefixViaNotNullConstraint() {
        graphQlTester.document("""
                {
                  searchBooks(titlePrefix: null) {
                    id
                    title
                  }
                }
                """)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getErrorType().toString())
                            .isEqualTo("ValidationError");
                });
    }

    @Test
    void shouldReturnMatchingBooksForValidTitlePrefix() {
        graphQlTester.document("""
                {
                  searchBooks(titlePrefix: "Clean") {
                    id
                    title
                  }
                }
                """)
                .execute()
                .path("searchBooks")
                .entityList(Object.class)
                .hasSize(1);
    }
}
