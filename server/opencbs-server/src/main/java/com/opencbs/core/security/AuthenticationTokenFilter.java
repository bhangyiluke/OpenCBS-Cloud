package com.opencbs.core.security;

import com.opencbs.core.configs.MultiReadRequest;
import com.opencbs.core.domain.User;
import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Pavel Bastov on 12/01/2017.
 */

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private TokenHelper tokenUtils;

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = new MultiReadRequest((HttpServletRequest) req);
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader == null) {
            chain.doFilter(httpRequest, res);
            return;
        }
        String token = authorizationHeader.substring("Bearer ".length());
        String username = this.tokenUtils.getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = this.userService.findByUsername(username).orElseThrow(()-> new BadCredentialsException("Invalid user name and password. Please try again"));
            if (StatusType.ACTIVE.equals(user.getStatusType()) && this.tokenUtils.verifyToken(token, user)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                tokenUtils.setEventInformation(user);
            }
        }

        chain.doFilter(httpRequest, res);
    }
}
