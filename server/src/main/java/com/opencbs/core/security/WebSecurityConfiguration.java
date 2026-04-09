package com.opencbs.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.opencbs.core.services.UserService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SuppressWarnings("unused")
public class WebSecurityConfiguration // extends WebSecurityConfigurerAdapter --- IGNORE ---
{

    // @Autowired
    // private EntryPointUnauthorizedHandler unauthorizedHandler;
    @Value("${cors.allowed-origins:http://localhost:4200,http://localhost:3000}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS,PATCH}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter(TokenHelper tokenHelper, UserService userService) {
        return new AuthenticationTokenFilter(tokenHelper, userService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity, AuthenticationTokenFilter tokenFilter)
            throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/*", "/assets/**", "/index.html", "/docs/**",
                                "/webjars/**", "/api/v3/api-docs/**",
                                "/api/swagger-ui/**", "/api/swagger-ui.html")
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // .requestMatchers("/api/users/current").permitAll()
                        .requestMatchers("/api/login", "/api/login/update-password",
                                "/api/login/password-reset")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/profiles/people/{personId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/profiles/companies/{companiesId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/profiles/groups/{groupId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/loan-applications/{loanApplicationId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/loans/{loanId}/attachments/{attachmentId}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/info").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/system-settings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/utils/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/utils/**").permitAll()
                        .anyRequest().authenticated())
                // .authenticationEntryPoint(this.unauthorizedHandler)

                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
