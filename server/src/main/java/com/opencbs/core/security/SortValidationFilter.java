package com.opencbs.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Validates Spring `sort` query parameters (e.g. `?sort=field,asc`) against a whitelist.
 * This is a focused defense against unsafe client-provided sort fields used by pageable
 * endpoints. It complements `RequestParamSanitizerFilter` which looks for SQL/HQL tokens.
 */
@Component
@Order(1)
public class SortValidationFilter extends HttpFilter {

    private static final Logger logger = LoggerFactory.getLogger(SortValidationFilter.class);

    // Conservative global whitelist of sortable properties. Add more entries as needed.
    private static final Set<String> GLOBAL_WHITELIST = new HashSet<>(Arrays.asList(
            "id", "createdAt", "updatedAt", "effectiveAt", "amount", "name", "number",
            "date", "branchId", "installmentNumber", "created_by", "createdById"
    ));

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String[] sortParams = request.getParameterValues("sort");
        if (sortParams != null) {
            for (String raw : sortParams) {
                if (raw == null || raw.isBlank()) continue;
                // Spring format: sort=property[,asc|desc]
                // Accept repeated sort params as well.
                String[] parts = raw.split(",");
                // first token is property (may be a path like entity.property)
                String property = parts[0].trim();
                if (property.isEmpty()) continue;

                // If property contains a path, use last segment
                if (property.contains(".")) {
                    property = property.substring(property.lastIndexOf('.') + 1);
                }

                // Basic safe-name check: allow only alphanumeric and underscore
                if (!property.matches("[A-Za-z0-9_]+")) {
                    logger.warn("Rejecting request with unsafe sort property: {} (uri={})", property, request.getRequestURI());
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid sort property");
                    return;
                }

                if (!GLOBAL_WHITELIST.contains(property)) {
                    logger.warn("Rejecting request with non-whitelisted sort property: {} (uri={})", property, request.getRequestURI());
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported sort property: " + property);
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
