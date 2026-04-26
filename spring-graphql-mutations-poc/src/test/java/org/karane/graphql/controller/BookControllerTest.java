package org.karane.graphql.controller;

import org.junit.jupiter.api.Test;
import org.karane.graphql.exception.ValidationExceptionResolver;
import org.karane.graphql.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@GraphQlTest(BookController.class)
@Import({BookService.class, ValidationExceptionResolver.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldListAllBooks() {
        graphQlTester.document("""
                {
                  books {
                    id
                    title
                    author { name }
                    pages
                  }
                }
                """)
                .execute()
                .path("books")
                .entityList(Object.class)
                .hasSize(3);
    }

    @Test
    void shouldGetBookById() {
        graphQlTester.document("""
                {
                  book(id: "1") {
                    id
                    title
                    author { name }
                    pages
                  }
                }
                """)
                .execute()
                .path("book.title")
                .entity(String.class)
                .isEqualTo("Clean Code");
    }

    @Test
    void shouldCreateBookSuccess() {
        graphQlTester.document("""
                mutation {
                  createBook(input: {
                    title: "The Pragmatic Programmer"
                    authorName: "David Thomas"
                    pages: 352
                  }) {
                    id
                    title
                    author { name }
                    pages
                  }
                }
                """)
                .execute()
                .path("createBook")
                .entity(Object.class)
                .satisfies(result -> {
                    assertThat(result).isNotNull();
                    assertThat(((java.util.Map<String, Object>) result).get("title"))
                            .isEqualTo("The Pragmatic Programmer");
                });
    }

    @Test
    void shouldCreateBookFailWithBlankTitle() {
        graphQlTester.document("""
                mutation {
                  createBook(input: {
                    title: ""
                    authorName: "David Thomas"
                    pages: 352
                  }) {
                    id
                    title
                  }
                }
                """)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getMessage())
                            .contains("title");
                });
    }

    @Test
    void shouldCreateBookFailWithTitleTooLong() {
        String longTitle = "a".repeat(201);
        graphQlTester.document(String.format("""
                mutation {
                  createBook(input: {
                    title: "%s"
                    authorName: "David Thomas"
                    pages: 352
                  }) {
                    id
                    title
                  }
                }
                """, longTitle))
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                    assertThat(errors.get(0).getMessage())
                            .contains("title");
                });
    }

    @Test
    void shouldUpdateBookSuccess() {
        graphQlTester.document("""
                mutation {
                  updateBook(id: "1", input: {
                    title: "Clean Code (Updated)"
                    pages: 500
                  }) {
                    id
                    title
                    pages
                  }
                }
                """)
                .execute()
                .path("updateBook.title")
                .entity(String.class)
                .isEqualTo("Clean Code (Updated)");
    }

    @Test
    void shouldDeleteBookSuccess() {
        graphQlTester.document("""
                mutation {
                  deleteBook(id: "1")
                }
                """)
                .execute()
                .path("deleteBook")
                .entity(Boolean.class)
                .isEqualTo(true);

        graphQlTester.document("""
                {
                  books {
                    id
                  }
                }
                """)
                .execute()
                .path("books")
                .entityList(Object.class)
                .hasSize(2);
    }

    @Test
    void shouldGetNonExistentBook() {
        graphQlTester.document("""
                {
                  book(id: "999") {
                    id
                    title
                  }
                }
                """)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).isNotEmpty();
                });
    }
}
