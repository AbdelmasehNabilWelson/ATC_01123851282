package org.example.eventbookingsystem.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JWTUtil {
    private String secretKey;
    private long expiration;

    @PostConstruct
    public void init() {
        secretKey = "EventSystemByAbdelmasehNabil"; // for developments purpose
        expiration = 3600000; // 1 hour in milliseconds
    }


    public String generateToken(String username, Map<String, Object> claims) {
        return JWT.create()
                .withSubject(username)
                .withPayload(claims)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secretKey));
    }


    public boolean validateToken(String token) {
        log.info("Validating JWT Token: {}", token);
        try {
            JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed: {}", e.getMessage());
            return false;
        }
    }


    public String getUsername(String token) {
        log.info("Extracting username from JWT Token: {}", token);
        String username = JWT.decode(token).getSubject();
        log.info("Extracted username: {}", username);
        return username;
    }

    public boolean isTokenExpired(String token) {
        log.info("Checking if JWT Token is expired: {}", token);
        Date expirationDate = JWT.decode(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    public String getClaim(String token, String claimKey) {
        log.info("Extracting claim from JWT Token: {} with key: {}", token, claimKey);
        return JWT.decode(token).getClaim(claimKey).asString();
    }
}
