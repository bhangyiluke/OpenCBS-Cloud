package com.opencbs.core.security;

import com.opencbs.core.domain.User;
import com.opencbs.core.domain.enums.SystemSettingsName;
import com.opencbs.core.helpers.DateHelper;
import com.opencbs.core.services.SystemSettingsService;
import com.opencbs.core.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collection;
import java.util.Date;

import org.mapstruct.ap.internal.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Pavel Bastov on 12/01/2017.
 */
@Component
public class TokenHelper {

    private static final String ISSUER = "com.opencbs.core";
    private final String SECRET_KEY = "AAAAC3NzaC1lZDI1NTE5AAAAIFr0LN2jIm/UKrCCpXuOHv8Nsoq1NX8DakauWMQiaaZo";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // private final SecretKeyProvider secretKeyProvider;

    private final UserService userService;

    private final SystemSettingsService systemSettingsService;

    @Autowired
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

        Claims claims = Jwts.claims();
        // .add(ISSUER, user)
        // .build();
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhoneNumber());
        claims.put("position", user.getPosition());
        claims.put("name", String.join(" ", user.getFirstName(), user.getLastName()));
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("permissions", user.getAuthorities());
        claims.put("roles", Collections.asSet(user.getRole()));
        // byte[] secretKey = this.secretKeyProvider.getKey();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setAudience(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        // return Jwts.builder()
        // .setSubject(user.getUsername())
        // .setIssuer(ISSUER)
        // .signWith(SignatureAlgorithm.HS512, secretKey)
        // .compact();
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();// .setSigningKey(SECRET_KEY).claims(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean IsSessionExpired(User user) {
        Integer minutes = Integer
                .valueOf(systemSettingsService.getValueByName(SystemSettingsName.EXPIRATION_SESSION_TIME_IN_MINUTES));
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
