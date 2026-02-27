package com.opencbs.core.security;

import java.util.Collections;
import java.util.Set;

/**
 * Utility to validate client-supplied sort fields before using them with Querydsl.
 *
 * Usage: call {@link #validate(String, Set)} and only use the returned value to build
 * OrderSpecifier or PathBuilder calls. This ensures only whitelisted fields are used.
 */
public final class SafeQuerydslOrder {

    private SafeQuerydslOrder() {
    }

    /**
     * Validate a requested sort field against an allow-list.
     *
     * @param requested the client-supplied field name
     * @param allowed a set of allowed field names (non-null)
     * @return the validated field name from the allow-list
     * @throws IllegalArgumentException if the field is not allowed
     */
    public static String validate(String requested, Set<String> allowed) {
        if (requested == null || requested.trim().isEmpty()) {
            throw new IllegalArgumentException("Sort field is empty");
        }
        if (allowed == null || allowed.isEmpty()) {
            throw new IllegalArgumentException("Allowed fields set must not be empty");
        }
        String normalized = requested.trim();
        if (allowed.contains(normalized)) {
            return normalized;
        }
        // Try case-insensitive match as a convenience
        for (String a : allowed) {
            if (a.equalsIgnoreCase(normalized)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Requested sort field is not permitted: " + requested);
    }

    /**
     * Convenience method to return an immutable allow-list from varargs.
     */
    public static Set<String> allow(String... fields) {
        if (fields == null || fields.length == 0) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(java.util.stream.Stream.of(fields).collect(java.util.stream.Collectors.toSet()));
    }
}
