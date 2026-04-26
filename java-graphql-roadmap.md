# GraphQL in Java Mastery Roadmap

## Level 1 — Schema Definition Language & Type System

- [ ] **`graphql-sdl-types-poc`** — Define schemas using SDL with scalar types (`String`, `Int`, `Float`, `Boolean`, `ID`), `type`, `enum`, `interface`, `union`, `input`, and `schema` keywords; parse and validate with `SchemaParser` and `TypeDefinitionRegistry` from `graphql-java`.
- [ ] **`graphql-custom-scalars-poc`** — Implement custom scalars (`Date`, `DateTime`, `UUID`, `BigDecimal`) using `Coercing<I, O>`, `GraphQLScalarType`, and `RuntimeWiring.newRuntimeWiring().scalar()`; register with `SchemaGenerator`.
- [ ] **`graphql-schema-directives-poc`** — Define and apply schema directives (`@deprecated`, `@auth`, `@length`) using `SchemaDirectiveWiring`, `SchemaDirectiveWiringEnvironment`, and `RuntimeWiring` directive registration.
- [ ] **`graphql-interfaces-unions-poc`** — Model polymorphic types with `interface` and `union` in SDL; implement `TypeResolver` to resolve concrete types at runtime; use `__typename` and inline fragments in queries.
- [ ] **`graphql-extended-scalars-poc`** — Use `graphql-java-extended-scalars` (`ExtendedScalars`); register `Date`, `DateTime`, `Time`, `UUID`, `BigDecimal`, `Url`, `Locale`, `JSON`, `Object` scalars; configure in Spring for GraphQL and DGS.
- [ ] **`graphql-pagination-relay-poc`** — Implement Relay-spec cursor-based pagination with `Connection`, `Edge`, `PageInfo`, `first`/`after`/`last`/`before` arguments, and `graphql-java-connection` utilities.

## Level 2 — graphql-java Core Engine

- [ ] **`graphql-java-wiring-poc`** — Build a schema programmatically with `TypeRuntimeWiring`, `RuntimeWiring`, `SchemaGenerator`, `GraphQLObjectType.Builder`, `GraphQLFieldDefinition`, and `GraphQL.Builder`; execute queries with `ExecutionInput` and `ExecutionResult`.
- [ ] **`graphql-data-fetcher-poc`** — Implement `DataFetcher<T>`, access `DataFetchingEnvironment` (arguments, context, source, field), use `PropertyDataFetcher` and `TrivialDataFetcher`; wire fetchers with `RuntimeWiring.newRuntimeWiring().type()`.
- [ ] **`graphql-execution-strategy-poc`** — Configure `AsyncExecutionStrategy`, `AsyncSerialExecutionStrategy`, and `SyncNonNullableValuesAsPromiseExecutionStrategy`; use `InstrumentationContext` and `SimplePerformantInstrumentation` to trace execution.
- [ ] **`graphql-java-context-poc`** — Pass request-scoped context via `ExecutionInput.newExecutionInput().graphQLContext()`, use `GraphQLContext.Builder`, propagate auth tokens and locale; access context in nested `DataFetcher`s.
- [ ] **`graphql-error-handling-poc`** — Implement `DataFetcherExceptionHandler`, `DataFetcherExceptionHandlerParameters`, `GraphqlErrorBuilder`, `ErrorType`; customize error extensions map and partial data responses (errors + data).
- [ ] **`graphql-java-instrumentation-poc`** — Build custom `Instrumentation` extending `SimplePerformantInstrumentation`; implement `beginExecution`, `beginField`, `beginParse`; chain with `ChainedInstrumentation`; add `MaxQueryComplexityInstrumentation` and `MaxQueryDepthInstrumentation`.

## Level 3 — Spring for GraphQL

- [x] **`spring-graphql-setup-poc`** — Bootstrap Spring for GraphQL with `spring-boot-starter-graphql`, configure `application.yml` (`spring.graphql.schema.locations`, `graphiql.enabled`), expose schema via HTTP/WebSocket transports using `GraphQlHttpHandler`.
- [x] **`spring-graphql-postgres-poc`** — Add persistence to Spring GraphQL with Spring Data JPA; define JPA entities (Book, Author) with `@Entity`, `@ManyToOne` relationships; create `JpaRepository` interfaces; configure PostgreSQL connection in `application.yml`; use Docker Compose for containerized database; seed initial data with `CommandLineRunner`.
- [x] **`spring-graphql-controllers-poc`** — Annotate controllers with `@Controller`, map queries with `@QueryMapping`, fields with `@SchemaMapping(typeName, field)`, pass arguments via `@Argument`, bind input types, and inject `Principal` or `DataFetchingEnvironment`.
- [x] **`spring-graphql-mutations-poc`** — Map mutations with `@MutationMapping`, validate input using Bean Validation (`@Valid`, `@NotNull`, `@Size`) on `@Argument`-annotated input DTOs, handle `ConstraintViolationException` via `DataFetcherExceptionResolverAdapter`.
- [ ] **`spring-graphql-subscriptions-poc`** — Implement subscriptions with `@SubscriptionMapping` returning `Flux<T>`; configure WebSocket transport with `spring.graphql.websocket.path`; use `WebSocketGraphQlTransport` for client-side testing.
- [ ] **`spring-graphql-batch-mapping-poc`** — Use `@BatchMapping` on controller methods returning `Map<S, T>` or `Mono<Map<S, T>>`; compare with manual `DataLoader` approach; combine with `@SchemaMapping` for nested field resolution.
- [ ] **`spring-graphql-testing-poc`** — Test with `@GraphQlTest`, `GraphQlTester`, `HttpGraphQlTester`, `WebSocketGraphQlTester`; use `documentName()`, `variable()`, `execute()`, `path()`, `entity()`, `matchesJson()`; mock services with `@MockBean`.

## Level 4 — Netflix DGS Framework

- [ ] **`dgs-setup-poc`** — Bootstrap Netflix DGS with `com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter`, configure `DgsSchemaProvider`, place schemas in `resources/schema/`; use `DgsQueryExecutor` to execute queries programmatically.
- [ ] **`dgs-data-fetchers-poc`** — Implement data fetchers with `@DgsComponent`, `@DgsQuery`, `@DgsMutation`, `@DgsSubscription`; use `@InputArgument` for typed argument binding, `DgsDataFetchingEnvironment`, and `@RequestHeader` / `@RequestParam` injection.
- [ ] **`dgs-data-loader-poc`** — Implement `MappedBatchLoaderWithContext<K, V>` registered with `@DgsDataLoader(name)`; use `DgsDataLoaderProvider`, `DataLoaderRegistry`, and `@DgsDataLoader(caching = true, batching = true)` to resolve N+1 via `DataLoader<K, V>.load(key)`.
- [ ] **`dgs-code-gen-poc`** — Generate type-safe Java/Kotlin POJOs and interfaces from SDL using `com.netflix.graphql.dgs.codegen` Gradle plugin; configure `generateClient = true`, `packageName`, `typeMapping`; use generated `DgsConstants` and client classes.
- [ ] **`dgs-custom-scalars-poc`** — Register custom scalars in DGS with `@DgsScalar(name)` on classes implementing `Coercing`; map Java types (`OffsetDateTime`, `UUID`) using `DgsCustomContextBuilder` and `DgsTypeDefinitionRegistry`.
- [ ] **`dgs-testing-poc`** — Write slice tests with `@SpringBootTest` + `DgsQueryExecutor`; use `GraphQLQueryRequest` and generated client types from codegen; assert with `JsonPath`; mock DGS components with `@MockBean`; test exceptions with `QueryException`.

## Level 5 — Data Loading & the N+1 Problem

- [ ] **`dataloader-batching-poc`** — Implement `BatchLoader<K, V>` and `MappedBatchLoader<K, V>` with `DataLoaderFactory`; register loaders in `DataLoaderRegistry`; schedule dispatch with `DispatchPredicate` and `DataLoaderOptions` (`maxBatchSize`, `cacheMap`).
- [ ] **`dataloader-caching-poc`** — Configure `DataLoaderOptions` with `CacheMap` (default `ConcurrentHashMap`) vs `CaffeineCache`; implement custom `CacheMap<K, V>`; handle cache invalidation per request scope; use `DataLoader.clearAll()` and `DataLoader.prime(key, value)`.
- [ ] **`dataloader-context-poc`** — Pass context to `BatchLoaderWithContext<K, V>` via `BatchLoaderContextProvider`; propagate `SecurityContext`, tenant ID, and locale; use `BatchLoaderEnvironment.getContext()` inside the batch function.
- [ ] **`graphql-java-jpa-poc`** — Integrate with Spring Data JPA using `EntityGraph` hints in `DataFetcher`s; use `@EntityGraph` and `JOIN FETCH` to avoid N+1; instrument queries with `hibernate.generate_statistics` to verify batch loading.
- [ ] **`scheduled-dataloader-poc`** — Use `ScheduledDataLoaderRegistry` with `TickerDispatchPredicate` for async dispatch timing; tune tick interval; compare throughput vs reactive `DispatchPredicate` under load.

## Level 6 — Input Validation & Error Handling

- [ ] **`graphql-input-validation-poc`** — Add server-side validation using `graphql-java-extended-validation`; configure `ValidationSchemaWiring`, use SDL directives (`@NotNull`, `@Size`, `@Pattern`, `@Range`); return structured validation errors with field paths via `GraphQLError.extensions`.
- [ ] **`spring-graphql-exception-resolver-poc`** — Implement `DataFetcherExceptionResolverAdapter` and `SubscriptionExceptionResolver`; map domain exceptions to `GraphQLError` with `ErrorType`, custom extensions, and HTTP status codes via `@GraphQlExceptionHandler`.
- [ ] **`graphql-partial-response-poc`** — Handle partial data responses (data + errors) from multiple `DataFetcher`s; configure `GraphQLError` list propagation in `ExecutionResult`; distinguish field errors from request errors in client response handling.
- [ ] **`graphql-null-safety-poc`** — Enforce non-null contracts in SDL with `!`; configure `NonNullableFieldWasNullError` propagation; use `@NotNull` on `@Argument` bindings; test null-bubble-up behavior across nested object types.

## Level 7 — Security

- [ ] **`graphql-spring-security-poc`** — Secure GraphQL endpoints with Spring Security; configure `SecurityFilterChain` for `/graphql`; extract JWT from `Authorization` header in `GraphQLContext`; use `@PreAuthorize` and `@Secured` on `@QueryMapping` / `@MutationMapping` methods.
- [ ] **`graphql-field-level-auth-poc`** — Implement field-level authorization using `SchemaDirectiveWiring` (`@auth`, `@hasRole` directives); intercept `DataFetcher` execution with `AuthorizationInstrumentation`; return partial responses or `null` for unauthorized fields.
- [ ] **`graphql-method-security-poc`** — Enable `@EnableMethodSecurity`; use `@PreAuthorize("hasRole('ADMIN')")`, `@PostAuthorize`, and `@PostFilter` on service methods called from `DataFetcher`s; test with `@WithMockUser`.
- [ ] **`graphql-query-depth-complexity-poc`** — Prevent abuse with `MaxQueryDepthInstrumentation(maxDepth)` and `MaxQueryComplexityInstrumentation(maxComplexity)`; implement custom `FieldComplexityCalculator`; add query cost analysis using `graphql-query-complexity` patterns.
- [ ] **`graphql-persisted-queries-poc`** — Implement Automatic Persisted Queries (APQ) with a Redis-backed `PreparsedDocumentProvider`; handle `PersistedQueryNotFound` / `PersistedQueryNotSupported` error codes; use `sha256` hashing; measure parse/validation savings.

## Level 8 — Subscriptions & Real-time

- [ ] **`spring-graphql-websocket-sub-poc`** — Implement WebSocket subscriptions with `@SubscriptionMapping` returning `Flux<T>`; configure the `graphql-ws` protocol; handle `connection_init`, `subscribe`, `complete`, and `ping/pong` lifecycle messages.
- [ ] **`spring-graphql-sse-poc`** — Expose subscriptions over Server-Sent Events using Spring WebFlux `ServerSentEvent<T>`; configure `HttpGraphQlHandler` with multipart/SSE transport; compare SSE vs WebSocket latency and connection overhead.
- [ ] **`dgs-subscriptions-poc`** — Implement DGS subscriptions with `@DgsSubscription` returning `Publisher<T>`; use `DgsReactiveWebSocketHandler`; publish events via `ApplicationEventPublisher` or Project Reactor `Sinks.Many<T>`.
- [ ] **`graphql-reactive-datasource-poc`** — Connect subscriptions to reactive data sources: `R2DBC` repositories, `ReactiveMongoRepository`, or Kafka via `ReactorKafkaReceiver`; transform with `Flux.map()`, `filter()`, `buffer()`, `window()`.
- [ ] **`graphql-subscription-filtering-poc`** — Add per-client subscription filtering using `Flux.filter()` with context-aware predicates; implement `@Argument`-based filters in `@DgsSubscription`; manage backpressure with `onBackpressureBuffer()` and `onBackpressureDrop()`.

## Level 9 — Performance & Observability

- [ ] **`graphql-query-caching-poc`** — Implement document-level query caching with `PreparsedDocumentProvider`; back with Caffeine (`CaffeineCache`) or Redis; measure parse/validation time savings; configure TTL and max-size eviction.
- [ ] **`graphql-micrometer-poc`** — Instrument GraphQL with Micrometer using `GraphQlTagsProvider`, `DefaultGraphQlTagsProvider`; export metrics (`graphql.request`, `graphql.error`, `graphql.datafetcher`) to Prometheus; build Grafana dashboards for latency and error rates.
- [ ] **`graphql-tracing-poc`** — Add distributed tracing with OpenTelemetry and `TracingInstrumentation`; propagate `TraceContext` through `GraphQLContext`; create spans per field resolution; export to Jaeger/Zipkin; correlate operation names with trace IDs.
- [ ] **`graphql-query-batching-poc`** — Handle request batching (multiple operations in one HTTP request) with `BatchedRequestSerializer`; configure Spring for GraphQL batch support; test with Apollo Client's `BatchHttpLink`; measure throughput improvement.
- [ ] **`graphql-introspection-control-poc`** — Disable or restrict introspection in production using `NoIntrospectionOperationPreparation`, custom `Instrumentation`, or Spring Security path rules; expose selective introspection via `__type` and `__schema` filtering.

## Level 10 — Schema Design Patterns

- [ ] **`graphql-schema-versioning-poc`** — Evolve schemas without breaking clients using `@deprecated(reason)`, additive field changes, nullable migrations; document breaking vs non-breaking changes; enforce validation in CI using `graphql-inspector` or Apollo Rover `schema check`.
- [ ] **`graphql-schema-stitching-poc`** — Merge multiple schemas using `TypeDefinitionRegistry.merge()`, `SchemaTransformer`, and `UnExecutableSchemaGenerator`; delegate field resolution across schemas with custom `DataFetcher`.
- [ ] **`graphql-schema-transformation-poc`** — Use `SchemaTransformer` to add, remove, or rename fields/types; implement `GraphQLTypeVisitor` with `TraverserContext`; apply transformations for multi-tenant schema customization and field aliasing.
- [ ] **`graphql-defer-stream-poc`** — Implement `@defer` and `@stream` incremental delivery using Spring for GraphQL multipart HTTP responses; return `Publisher<ExecutionResult>` from deferred resolvers; handle chunked transfer encoding.
- [ ] **`graphql-file-upload-poc`** — Handle multipart file uploads with `graphql-multipart-request-spec`; configure `MultipartVariablesMapper`; map `Upload` scalar to `MultipartFile`; process uploads with Spring's `FilePart` in reactive handlers.

## Level 11 — Integration with Data Technologies

- [ ] **`graphql-spring-data-jpa-poc`** — Integrate Spring for GraphQL with Spring Data JPA; use `JpaSpecificationExecutor`; map `@Argument` filter inputs to `Specification<T>`; implement cursor pagination with `ScrollPosition` and `Window<T>` (Spring Data 3).
- [ ] **`graphql-r2dbc-reactive-poc`** — Build a fully reactive API with Spring Data R2DBC, `ReactiveCrudRepository`, `DatabaseClient`; return `Mono<T>` and `Flux<T>` from `@QueryMapping`; use `@Transactional` with reactive `TransactionalOperator`.
- [ ] **`graphql-mongodb-poc`** — Connect GraphQL to MongoDB with Spring Data MongoDB (`ReactiveMongoTemplate`); map nested document structures to GraphQL object types; implement text search and aggregation pipeline queries from `DataFetcher`s.
- [ ] **`graphql-kafka-poc`** — Bridge subscriptions to Kafka using `spring-kafka` `KafkaListener` publishing to `Sinks.Many<T>`; implement `@SubscriptionMapping` consuming from `Flux`; handle consumer group offsets and deserialization with `JsonDeserializer`.
- [ ] **`graphql-redis-caching-poc`** — Add Redis-backed field-level caching in `DataFetcher`s using `ReactiveRedisTemplate`; implement cache-aside with `Mono.fromCallable()` + TTL; serialize GraphQL response subsets as JSON in Redis hash structures.

## Level 12 — Testing Strategies

- [ ] **`graphql-unit-test-poc`** — Unit test `DataFetcher` implementations with JUnit 5 and Mockito; build `DataFetchingEnvironment` using `DataFetchingEnvironmentImpl.Builder`; assert `ExecutionResult` data and errors; test `TypeResolver` with mock `TypeResolutionEnvironment`.
- [ ] **`spring-graphql-slice-test-poc`** — Use `@GraphQlTest(controllers = ...)` to test controllers in isolation; configure `RuntimeWiringConfigurer` beans; test queries/mutations/subscriptions with `GraphQlTester`; verify argument binding and error propagation.
- [ ] **`spring-graphql-integration-test-poc`** — Run full integration tests with `@SpringBootTest(webEnvironment = RANDOM_PORT)`; use `HttpGraphQlTester`; test subscription `Flux` with `StepVerifier`; assert schema introspection responses.
- [ ] **`dgs-integration-test-poc`** — Test DGS with `DgsQueryExecutor.executeAndExtractJsonPath()`; use generated `*GraphQLQuery` and `*ProjectionRoot` from codegen; validate response shapes; use `@SpringBootTest` with `EmbeddedKafka` for subscription testing.
- [ ] **`graphql-contract-test-poc`** — Implement consumer-driven contract tests using Pact or Spring Cloud Contract; generate GraphQL pacts from schema; verify provider compliance; integrate with CI using `pact-jvm`.

## Level 13 — Federation & Microservices

- [ ] **`apollo-federation-v2-poc`** — Build an Apollo Federation v2 subgraph with Spring for GraphQL using `spring-graphql-federation`; implement `@EntityMapping` for `@key` entity resolution; configure `@Shareable`, `@Override`, `@Inaccessible`; register with Apollo Router.
- [ ] **`dgs-federation-poc`** — Create a DGS federated subgraph with `graphql-dgs-federation-compatibility`; implement `@DgsEntityFetcher`, `@DgsTypeResolver`; test federation compliance with `FederationSdlQueryHandler`; compose schemas using Apollo Rover CLI.
- [ ] **`graphql-gateway-poc`** — Build a GraphQL gateway aggregating multiple services using schema stitching or Apollo Router; implement remote `DataFetcher`s with `WebClient`; handle cross-service type merging and `@external` field delegation.
- [ ] **`graphql-microservice-auth-poc`** — Propagate JWT / OAuth2 tokens across federated services; share `SecurityContext` via `GraphQLContext` in subgraph `DataFetcher`s; implement service-to-service auth with `ClientCredentials` grant and `WebClient` interceptors.
- [ ] **`graphql-schema-registry-poc`** — Set up a schema registry for federated schema versioning; automate schema push/check in CI with Apollo Rover; enforce breaking-change detection; version-control SDL alongside service deployments.

## Level 14 — Real-World End-to-End Projects

- [ ] **`graphql-ecommerce-api-poc`** — Build a full e-commerce GraphQL API with Spring for GraphQL + JPA: product catalog (query/filter/paginate with `ScrollPosition`), cart mutations, order subscriptions, JWT auth with `@PreAuthorize`, N+1 solved via `@BatchMapping`, Micrometer metrics exported to Prometheus.
- [ ] **`graphql-social-feed-poc`** — Implement a social feed service with Netflix DGS: user follows, post feed queries with cursor pagination, real-time feed updates via WebSocket subscriptions backed by Kafka `Sinks.Many<T>`, Redis caching, `@auth` directive field-level authorization.
- [ ] **`graphql-federated-shop-poc`** — Compose a federated shop from three DGS microservices (users, catalog, orders) with Apollo Router; implement cross-service `@EntityMapping`, propagate OAuth2 tokens via `GraphQLContext`, test with Apollo Sandbox and consumer-driven contract tests.
- [ ] **`graphql-reporting-api-poc`** — Build a reporting API: aggregate JPA queries mapped to `Connection` types, `@defer` for large result sets, `MaxQueryComplexityInstrumentation`, persisted queries backed by Redis, OpenTelemetry tracing, Grafana dashboard.
- [ ] **`graphql-multi-tenant-poc`** — Design a multi-tenant platform: tenant resolution from JWT claims, per-tenant schema customization via `SchemaTransformer`, tenant-scoped `DataLoader` caches, row-level security in JPA with Hibernate `@Filter`, audit logging via `ChainedInstrumentation`.
