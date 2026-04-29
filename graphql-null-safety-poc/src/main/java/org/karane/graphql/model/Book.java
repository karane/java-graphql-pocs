package org.karane.graphql.model;

public record Book(String id, String title, Author author, Integer pages, Review review) {}
