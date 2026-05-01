# spring-graphql-controllers-poc

Demonstrates three advanced Spring for GraphQL controller features:
1. **Input DTO binding** — `@Argument` on complex input types (`BookFilter`)
2. **Principal injection** — Security context access in GraphQL methods
3. **DataFetchingEnvironment injection** — Low-level GraphQL runtime access

## Stack

- Spring Boot 3.4.4, Java 21
- Spring for GraphQL 
- Spring Security
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

Six tests covering input DTO binding, Principal injection, and DataFetchingEnvironment extraction.

## Queries

### Input DTO: books with filter
```graphql
query {
  books(filter: { titleContains: "Clean" }) {
    id
    title
    author {
      name
    }
  }
}
```

```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ books(filter: { titleContains: \"Clean\" }) { id title author { name } } }"}' | jq
```

### Input DTO: books filtered by author
```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ books(filter: { authorId: \"1\" }) { id title author { name } } }"}' | jq
```

### Principal: current user
```graphql
query {
  me
}
```

With HTTP Basic auth: `user` / `password` (configured in `application.yml`).

```bash
curl -s -X POST http://localhost:8080/graphql \
  -u user:password \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ me }"}' | jq
```

### DataFetchingEnvironment: field info
```graphql
query {
  environment {
    field
    locale
    selectedFields
  }
}
```

Returns the GraphQL field name, locale, and list of selected subfields from the environment.

```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ environment { field locale selectedFields } }"}' | jq
```
