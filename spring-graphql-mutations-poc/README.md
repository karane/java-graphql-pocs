# spring-graphql-mutations-poc

Demonstrates three Spring for GraphQL mutation features:
1. **`@MutationMapping`** — Handler methods for GraphQL mutations
2. **Bean Validation** — Input validation with `@Valid`, `@NotBlank`, `@Size`, `@Min`, `@Max` on input DTOs
3. **Exception resolution** — `DataFetcherExceptionResolverAdapter` to catch `ConstraintViolationException` and return structured validation errors

## Stack

- Spring Boot 3.4.4, Java 21
- Spring for GraphQL (GraphQL via HTTP/WebSocket)
- Spring Validation (Bean Validation / Jakarta Validation)
- In-memory data store

## Run

```bash
mvn spring-boot:run
```

Server runs on `http://localhost:8080`, GraphiQL at `http://localhost:8080/graphiql?path=/graphql&wsPath=/graphql-ws`.

## Test

```bash
mvn test
```

Nine tests covering successful mutations, validation errors, and query operations.

## Mutations

### Create Book (with validation)
```graphql
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
```

Validation: `title` and `authorName` required, max 200 and 100 chars respectively; `pages` must be 1–5000.

### Create Book with Validation Error
```graphql
mutation {
  createBook(input: {
    title: ""
    authorName: "David"
    pages: 352
  }) {
    id
  }
}
```

Response:
```json
{
  "errors": [
    {
      "message": "title: must not be blank",
      "extensions": {
        "errorType": "BAD_REQUEST"
      }
    }
  ]
}
```

### Update Book
```graphql
mutation {
  updateBook(id: "1", input: {
    title: "Clean Code (2nd Edition)"
    pages: 500
  }) {
    id
    title
    author { name }
    pages
  }
}
```

### Delete Book
```graphql
mutation {
  deleteBook(id: "1")
}
```

## Queries

### List all books
```graphql
{
  books {
    id
    title
    author { name }
    pages
  }
}
```

### Get single book by ID
```graphql
{
  book(id: "1") {
    id
    title
    author { name }
    pages
  }
}
```

## Key Implementation Details

**`@Controller @Validated`** on `BookController` — enables Bean Validation on method parameters.

**`@Argument @Valid CreateBookInput input`** — triggers validation of the input DTO; constraint violations raise `ConstraintViolationException`.

**`ValidationExceptionResolver extends DataFetcherExceptionResolverAdapter`** — catches `ConstraintViolationException`, extracts violation messages, and returns a `GraphQLError` with `ErrorType.BAD_REQUEST`.

**Input DTOs** — Java records with `@NotBlank`, `@Size`, `@Min`, `@Max` annotations from Jakarta Validation.
