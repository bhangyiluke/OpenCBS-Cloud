package com.opencbs.core.security;

import com.opencbs.core.domain.User;
import com.opencbs.core.domain.enums.SystemSettingsName;
import com.opencbs.core.helpers.DateHelper;
import com.opencbs.core.services.SystemSettingsService;
import com.opencbs.core.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

import java.util.Collections;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * Created by Pavel Bastov on 12/01/2017.
 */
@Component
public class TokenHelper {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TokenHelper.class);
    private static final String ISSUER = "com.opencbs.core";
    private final String SECRET_KEY = "AAAAC3NzaC1lZDI1NTE5AAAAIFr0LN2jIm/UKrCCpXuOHv8Nsoq1NX8DakauWMQiaaZo";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // private final SecretKeyProvider secretKeyProvider;

    private final UserService userService;

    private final SystemSettingsService systemSettingsService;

    //@Autowired
    public TokenHelper(SecretKeyProvider secretKeyProvider,
            UserService userService,
            SystemSettingsService systemSettingsService) {
        // this.secretKeyProvider = secretKeyProvider;
        this.userService = userService;
        this.systemSettingsService = systemSettingsService;
    }

    public String getUsernameFromToken(String token) {
        try {
            return this.getClaimsFromToken(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean verifyToken(String token, User user) {
        final String username = this.getUsernameFromToken(token);
        logger.info("Verifying token for user: {}", username);
        if (username == null)
            return false;
        if (username.equals(user.getUsername()) && !IsSessionExpired(user)) {
            return true;
        }

        return false;
    }

    public String tokenFor(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhoneNumber());
        claims.put("position", user.getPosition());
        claims.put("name", String.join(" ", user.getFirstName(), user.getLastName()));
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("permissions", user.getAuthorities());
        claims.put("roles", Collections.singleton(user.getRole()));
        // byte[] secretKey = this.secretKeyProvider.getKey();
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
                .issuer(ISSUER)
                .claims(claims)
                .subject(user.getUsername())
                .audience().add(user.getId().toString())
                .and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean IsSessionExpired(User user) {
        Integer minutes = Integer
                .valueOf(systemSettingsService.getValueByName(SystemSettingsName.EXPIRATION_SESSION_TIME_IN_MINUTES));
        logger.info("Checking if session is expired for user: {}. Last entry time: {}, Expiration time in minutes: {}",
                user.getUsername(), user.getLastEntryTime(), minutes);
                if (minutes == 0) { // session never ended
            return false;
        }
        if (DateHelper.greater(DateHelper.getLocalDateTimeNow(), user.getLastEntryTime().plusMinutes(minutes))) {
            return true;
        }

        return false;
    }

    public void setEventInformation(User user) {
        user.setLastEntryTime(DateHelper.getLocalDateTimeNow());
        userService.update(user);
    }
}
