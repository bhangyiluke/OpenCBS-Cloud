package com.opencbs.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import com.opencbs.core.configs.MultiReadRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Simple request parameter sanitizer filter.
 *
 * This performs a basic check for suspicious SQL/HQL keywords in request
 * parameter values
 * (for example, in `orderBy` or similar fields) and rejects requests that
 * contain them.
 *
 * This is a defense-in-depth measure to reduce the risk of injection when
 * libraries
 * that build queries from strings are present.
 */
@Component
public class RequestParamSanitizerFilter extends HttpFilter {
    private static final List<String> SUSPICIOUS = Arrays.asList(
            "SELECT", "UNION", "INTERSECT", "--", ";", "/*", "*/", "1=1", "DROP", "INSERT", "DELETE", "UPDATE");

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest mr = new MultiReadRequest(request);
        Map<String, String[]> params = mr.getParameterMap();
        for (Map.Entry<String, String[]> e : params.entrySet()) {
            for (String v : e.getValue()) {
                if (v == null)
                    continue;
                String up = v.toUpperCase();
                for (String s : SUSPICIOUS) {
                    if (up.contains(s)) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Suspicious request parameter detected");
                        return;
                    }
                }
            }
        }
        chain.doFilter(mr, response);
    }
}
