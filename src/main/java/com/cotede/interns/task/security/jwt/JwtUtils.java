package com.cotede.interns.task.security.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
@Getter
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    @Value("${spring.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    public String getJwtFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
    private Key key() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        Optional.of(keyBytes)
                .filter(bytes -> bytes.length >=32)
                .orElseThrow(() ->new IllegalArgumentException("JWT secret key must be at least 256 bits long."));
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            AuthMessageThread.setAuthError("jwt.token.invalid");
        } catch (ExpiredJwtException e) {
            AuthMessageThread.setAuthError("jwt.token.expired");
        } catch (UnsupportedJwtException e) {
            AuthMessageThread.setAuthError("jwt.token.unsupported");
        } catch (IllegalArgumentException e) {
            AuthMessageThread.setAuthError("jwt.token.signature.invalid");
        } catch (SignatureException e) {
            AuthMessageThread.setAuthError("jwt.token.signature.invalid");
        }
        throw new RuntimeException();
    }
}