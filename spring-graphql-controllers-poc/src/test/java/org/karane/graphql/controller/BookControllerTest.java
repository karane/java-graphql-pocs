package org.karane.graphql.controller;

import org.junit.jupiter.api.Test;
import org.karane.graphql.model.Author;
import org.karane.graphql.model.Book;
import org.karane.graphql.model.BookFilter;
import org.karane.graphql.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(BookController.class)
@Import(BookService.class)
class BookControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    void booksQuery_noFilter_returnsAll() {
        graphQlTester.document("""
                query {
                  books {
                    id
                    title
                    author {
                      name
                    }
                  }
                }
                """)
                .execute()
                .path("books")
                .entityList(Object.class)
                .hasSize(4);
    }

    @Test
    void booksQuery_withTitleFilter() {
        graphQlTester.document("""
                query {
                  books(filter: { titleContains: "Clean" }) {
                    id
                    title
                    author {
                      name
                    }
                  }
                }
                """)
                .execute()
                .path("books")
                .entityList(Object.class)
                .hasSize(2);
    }

    @Test
    void booksQuery_withAuthorIdFilter() {
        graphQlTester.document("""
                query {
                  books(filter: { authorId: "1" }) {
                    id
                    title
                    author {
                      name
                    }
                  }
                }
                """)
                .execute()
                .path("books")
                .entityList(Object.class)
                .hasSize(2);
    }

    @Test
    void environmentQuery_returnsFieldAndSelectedFields() {
        graphQlTester.document("""
                query {
                  environment {
                    field
                    locale
                    selectedFields
                  }
                }
                """)
                .execute()
                .path("environment.field").entity(String.class).isEqualTo("environment")
                .path("environment.selectedFields").entityList(String.class).hasSize(3);
    }
}
