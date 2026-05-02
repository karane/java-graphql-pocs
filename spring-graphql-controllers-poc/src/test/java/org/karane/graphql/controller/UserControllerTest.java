package org.karane.graphql.controller;

import org.junit.jupiter.api.Test;
import org.karane.graphql.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

@GraphQlTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    @WithMockUser(username = "alice")
    void meQuery_authenticated_returnsUsername() {
        graphQlTester.document("""
                query {
                  me
                }
                """)
                .execute()
                .path("me").entity(String.class).isEqualTo("alice");
    }

    @Test
    @WithAnonymousUser
    void meQuery_anonymous_returnsAnonymous() {
        graphQlTester.document("""
                query {
                  me
                }
                """)
                .execute()
                .path("me").entity(String.class).isEqualTo("anonymous");
    }
}
