package com.opencbs.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SuppressWarnings("unused")
public class WebSecurityConfiguration // extends WebSecurityConfigurerAdapter --- IGNORE ---
{

    @Autowired
    private EntryPointUnauthorizedHandler unauthorizedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean(AuthenticationManager manager) throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(manager);
        return authenticationTokenFilter;
    }

    // @Override
    // public void configure(WebSecurity web) throws Exception {
    // web.ignoring().antMatchers("/*", "/assets/**", "index.html", "/docs/**",
    // "/webjars/**", "/v2/api-docs/**",
    // "/swagger-resources/**");
    // }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity, AuthenticationTokenFilter tokenFilter)
            throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/*", "/assets/**", "index.html", "/docs/**", "/webjars/**", "/v2/api-docs/**",
                                "/swagger-resources/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api").permitAll()
                        .requestMatchers("/api/login", "/api/login/update-password", "/api/login/password-reset")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profiles/people/{personId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/profiles/companies/{companiesId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profiles/groups/{groupId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/loan-applications/{loanApplicationId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/loans/{loanId}/attachments/{attachmentId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/info").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/system-settings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/utils/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/utils/**").permitAll()
                        .anyRequest().authenticated())
                // .authenticationEntryPoint(this.unauthorizedHandler)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
