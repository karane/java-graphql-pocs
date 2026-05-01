package org.karane.graphql.model;

import java.util.List;

public record EnvironmentInfo(String field, String locale, List<String> selectedFields) {}
