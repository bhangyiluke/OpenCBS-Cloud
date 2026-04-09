package com.opencbs.core.security;

import com.opencbs.core.configs.MultiReadRequest;
import com.opencbs.core.domain.User;
import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Created by Pavel Bastov on 12/01/2017.
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    private final TokenHelper tokenUtils;
    private final UserService userService;

    public AuthenticationTokenFilter(TokenHelper tokenUtils, UserService userService) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = new MultiReadRequest((HttpServletRequest) request);
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader == null) {
            filterChain.doFilter(httpRequest, response);
            return;
        }
        String token = authorizationHeader.substring("Bearer ".length());
        String username = this.tokenUtils.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = this.userService.findByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("Invalid user name and password. Please try again"));
            if (StatusType.ACTIVE.equals(user.getStatusType()) && this.tokenUtils.verifyToken(token, user)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                tokenUtils.setEventInformation(user);
                logger.info("Validating jwt for user: " + username + " - SUCCESS");
            } else {
                logger.info("Validating jwt for user: " + username + " - FAILED");
            }
        }

        filterChain.doFilter(httpRequest, response);
    }
}
