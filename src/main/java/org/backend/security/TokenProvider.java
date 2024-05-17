package org.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.security.Key;

@Slf4j
@Component
public class TokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret = "secret-secret-secret-secret-secret-secret-secret-secret-secret-secret-secret-";

    @Value("${app.jwt.expiration.minutes}")
    private Long jwtExpirationMinutes = 1440L;
    
    private Key getSigningKey() {
    	byte[] keyBytes = jwtSecret.getBytes();
    	return Keys.hmacShaKeyFor(keyBytes);
	}
    
    public String generate(Authentication authentication) {
    	if (authentication == null) return null;
    	
    	String principal = (String) authentication.getPrincipal();
        Key signingKey = getSigningKey();
    	
        long expiretime = jwtExpirationMinutes;
		return Jwts.builder()
                .setHeaderParam("type", TOKEN_TYPE)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expiretime).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(principal)
                .claim("email", principal)
                .claim("password", authentication.getCredentials())
                .compact();
    }
    private String getRolesAsString(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
    public Optional<Jws<Claims>> validateTokenAndGetJws(String token) {
        try {
            Key signingKey = getSigningKey();

            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            
            return Optional.of(jws);
        } catch (ExpiredJwtException exception) {
//            log.error("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
//            Logger.("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
//            log.error("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
//            log.error("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        return Optional.empty();
    }

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "sms-activate-api";
    public static final String TOKEN_AUDIENCE = "sms-activate-app";
}
