package com.bithumb.jwttest.config.auth.jwt;

import com.bithumb.jwttest.config.auth.LoginUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${bithumb.jwt.secret}")
    private String secret;

    @Value("${bithumb.jwt.expiration-ms}")
    private int expirationMilliseconds;

    public String generateJwtToken(LoginUser loginUser) {

        return Jwts.builder()
                .setSubject((loginUser.getMember().getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
