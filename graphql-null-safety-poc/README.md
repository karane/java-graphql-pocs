# GraphQL Null Safety POC

Demonstrates four null-safety mechanisms in Spring for GraphQL:

1. **Non-null `!` in SDL** — Fields marked with `!` enforce non-null contracts at the schema level
2. **`NonNullableFieldWasNullError` propagation** — When a `!` field resolves to null, the error bubbles up through parent types until it reaches a nullable ancestor
3. **`@NotNull` on `@Argument` bindings** — Enforce non-null on SDL-nullable arguments at the Java validation layer
4. **Null bubble-up across nested types** — How nullification cascades differently depending on whether each ancestor field is nullable or non-null

## Stack

- Spring Boot 3.4.4, Java 21
- Spring for GraphQL

## Run

```bash
mvn spring-boot:run
```

Server at `http://localhost:8080`, GraphiQL at `http://localhost:8080/graphiql?path=/graphql`.

## Testing

### GraphiQL

Open `http://localhost:8080/graphiql?path=/graphql` in a browser and paste any query from the examples below.

---

### curl

All requests are `POST` to `http://localhost:8080/graphql` with `Content-Type: application/json`.

**Fetch a valid book (no nulls)**
```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ book(id: \"1\") { id title author { name bio } pages } }"}' | jq
```

**Scenario A — deep null bubble-up (`Review.comment` is null)**
```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ book(id: \"2\") { id title review { rating comment } } }"}' | jq
```
Expected: `data.book` is `null`, error path `["book","review","comment"]`.

**Scenario B — null stops at nullable ancestor (`Publisher.name` is null)**
```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ book(id: \"3\") { id author { name publisher { name country } } } }"}' | jq
```
Expected: `data.book.author.publisher` is `null`, Book and Author remain valid.

**Scenario C — null bubbles to root, `data` becomes null (`books` list)**
```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ books { id title review { rating comment } } }"}' | jq
```
Expected: `data` is `null`, error path `["books", 1, "review", "comment"]`.

**Scenario D — `@NotNull` on SDL-nullable argument**
```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ searchBooks(titlePrefix: null) { id title } }"}' | jq
```
Expected: `errors[0].extensions.classification` is `"ValidationError"`.

**Valid `searchBooks`**
```bash
curl -s -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ searchBooks(titlePrefix: \"Cl\") { id title } }"}' | jq
```

