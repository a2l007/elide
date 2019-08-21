/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package com.yahoo.elide.contrib.testhelpers.graphql.elements;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

/**
 * {@link Node} represents the same concepts as the "node" in Relay's connection pattern
 * (https://graphql.org/learn/pagination/#pagination-and-edges).
 */
@RequiredArgsConstructor
public class Node implements Selection {

    private static final long serialVersionUID = 2525022170227460587L;

    /**
     * The selections inside the node.
     * <p>
     * For example
     *
     */
    @NonNull
    @Getter
    private final SelectionSet fields;

    @Getter(AccessLevel.PRIVATE)
    private final boolean queryNode;

    @Override
    public String toGraphQLSpec() {
        return isQueryNode() ? toQuerySpec() : toResponseSpec();
    }

    private String toQuerySpec() {
        return String.format(
                "node %s",
                getFields().toGraphQLSpec()
        );
    }

    private String toResponseSpec() {
        return String.format("{\"node\":{%s}}", serializeNode(this));
    }

    /**
     * Serializes a node represented by a {@link SelectionSet} into GraphQL response data format.
     *
     * @param node  An object containing all fields of an instantiated entity
     *
     * @return a sub-string of response data
     */
    private static String serializeNode(Node node) {
        return node.getFields().getSelections().stream()
                .map(Selection::toGraphQLSpec)
                .collect(Collectors.joining(","));
    }
}
