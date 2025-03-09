package com.example.demo.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtil {
//    @Value("${jwt.secret}")

    @Autowired
    private SecretKeyGenerator secretKeyGenerator;

    private String secretKey; // You should move this to an environment variable.

    @PostConstruct
    public void init() {
        this.secretKey = secretKeyGenerator.generateSecretKey();
    }

        // Generate JWT Token
        public String generateToken(String username) {
            return Jwts .builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }

        // Validate the Token
        public boolean validateToken(String token, String username) {
            String extractedUsername = extractUsername(token);
            return (username.equals(extractedUsername) && !isTokenExpired(token));
        }

        // Extract Username from Token
        public String extractUsername(String token) {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        // Check if Token is Expired
        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        // Extract Expiration Date from Token
        private Date extractExpiration(String token) {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        }

}
