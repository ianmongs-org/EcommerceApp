package com.java.EcomerceApp.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header received: {}", 
                     bearerToken != null ? 
                     (bearerToken.length() > 15 ? 
                      bearerToken.substring(0, 10) + "..." : bearerToken) : 
                     "null");
                      
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // Remove Bearer prefix
            logger.debug("Extracted JWT token (masked): {}", 
                        token.length() > 10 ? 
                        token.substring(0, 5) + "..." + token.substring(token.length() - 5) : 
                        "INVALID_TOKEN");
            return token;
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        logger.info("Generating JWT token for user: {}", username);
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        
        logger.debug("Token will expire at: {}", expiryDate);
        
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
                
        logger.debug("Generated token (masked): {}", 
                    token.length() > 10 ? 
                    token.substring(0, 5) + "..." + token.substring(token.length() - 5) : 
                    "INVALID_TOKEN");
                    
        return token;
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            String username = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
                    
            logger.debug("Username extracted from JWT: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    private Key key() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        logger.debug("JWT Secret key length: {} bytes", keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateJwtToken(String authToken) {
        logger.debug("Validating JWT token...");
        
        if (authToken == null) {
            logger.error("JWT token is null");
            return false;
        }
        
        try {
            // Check token format before parsing
            String[] chunks = authToken.split("\\.");
            if (chunks.length != 3) {
                logger.error("Invalid JWT token format. Token should have 3 parts but has: {}", chunks.length);
                return false;
            }
            
            // Parse and validate token
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(authToken);
                
            logger.info("JWT token is valid");
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during JWT validation: {}", e.getMessage());
        }
        
        return false;
    }
    
    // Print diagnostics about a JWT token without exposing sensitive data
    public void printTokenDiagnostics(String token) {
        try {
            if (token == null || token.isEmpty()) {
                logger.info("Token diagnostics: Token is null or empty");
                return;
            }
            
            String[] chunks = token.split("\\.");
            logger.info("Token diagnostics: Number of segments: {}", chunks.length);
            
            if (chunks.length == 3) {
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
                    
                logger.info("Token diagnostics: Subject: {}", claims.getSubject());
                logger.info("Token diagnostics: Issuer: {}", claims.getIssuer());
                logger.info("Token diagnostics: Issued At: {}", claims.getIssuedAt());
                logger.info("Token diagnostics: Expiration: {}", claims.getExpiration());
            }
        } catch (Exception e) {
            logger.error("Token diagnostics: Error parsing token: {}", e.getMessage());
        }
    }
}