# spring-graphql-setup-poc

## Stack

- Spring Boot 3.4.4
- Spring for GraphQL (spring-boot-starter-graphql)
- Java 21

## Running

```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`. GraphiQL UI available at:

```
http://localhost:8080/graphiql?path=/graphql&wsPath=/graphql-ws
```

## Testing via GraphiQL

Open the URL above in your browser. You'll get an interactive query editor.

> **Note:** the `path` and `wsPath` query params are required — without them the UI defaults to `/graphql` for both HTTP and WebSocket, which works for queries/mutations but won't resolve the correct WebSocket path.

Paste any of the queries below directly into the editor panel (no `curl`, no JSON wrapper):

**List all books**

```graphql
{
  books {
    id
    title
    author { id name }
  }
}
```

**Get a single book by ID**

```graphql
{
  book(id: "2") {
    id
    title
    author { name }
  }
}
```

**Get books by author**

```graphql
{
  booksByAuthor(authorId: "2") {
    id
    title
  }
}
```

**Add a book (mutation)**

```graphql
mutation {
  addBook(title: "Domain-Driven Design", authorId: "1") {
    id
    title
    author { name }
  }
}
```

**Trigger an error (unknown author)**

```graphql
mutation {
  addBook(title: "Ghost", authorId: "999") {
    id
  }
}
```

## API Examples

All requests are `POST /graphql` with `Content-Type: application/json`.

### Queries

**List all books (with nested author via `@SchemaMapping`)**

```bash
curl -s http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ books { id title author { id name } } }"}' | jq .
```

**Get a single book by ID**

```bash
curl -s http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ book(id: \"2\") { id title author { name } } }"}' | jq .
```

---

**Get books by author**

```bash
curl -s http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ booksByAuthor(authorId: \"2\") { id title } }"}' | jq .
```

---

**Query a non-existent book (nullable field returns null, no error)**

```bash
curl -s http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ book(id: \"999\") { id title } }"}' | jq .
```

### Mutations

**Add a new book**

```bash
curl -s http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"mutation { addBook(title: \"Domain-Driven Design\", authorId: \"1\") { id title author { name } } }"}' | jq .
```

**Add a book with an unknown author (BAD_REQUEST error)**

```bash
curl -s http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"mutation { addBook(title: \"Ghost\", authorId: \"999\") { id } }"}' | jq .
```
