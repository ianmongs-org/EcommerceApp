package com.java.EcomerceApp.security.jwt;

import com.java.EcomerceApp.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Log request details for debugging
            String uri = request.getRequestURI();
            String method = request.getMethod();
            logger.info("Processing request: {} {}", method, uri);
            
            // Extract JWT token
            String jwt = parseJwt(request);
            if (jwt != null) {
                logger.info("JWT token found in request for {}: {}", uri, maskToken(jwt));
                
                // Validate token
                boolean isValid = jwtUtils.validateJwtToken(jwt);
                logger.info("JWT validation result for {}: {}", uri, isValid);
                
                if (isValid) {
                    // Extract username from token
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    logger.info("Username from JWT: {}", username);
                    
                    // Load user details
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.info("User found: {}, Enabled: {}, Roles: {}", 
                                userDetails.getUsername(), 
                                userDetails.isEnabled(),
                                userDetails.getAuthorities());
                    
                    // Create authentication token
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Authentication set in SecurityContext for: {}", username);
                } else {
                    logger.warn("JWT token validation failed for URI: {}", uri);
                }
            } else {
                logger.debug("No JWT token found in request headers for URI: {}", uri);
            }
        } catch (Exception e) {
            logger.error("Authentication error: Cannot set user authentication for request: {}", 
                        request.getRequestURI(), e);
        }

        // Add CORS headers for development
        addCorsHeaders(response);
        
        // Continue with filter chain
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt != null) {
            logger.debug("JWT found in request header: {}", maskToken(jwt));
        } else {
            logger.debug("No JWT found in request header");
        }
        return jwt;
    }
    
    // Helper to log tokens without exposing full content
    private String maskToken(String token) {
        if (token == null || token.length() < 10) return "INVALID_TOKEN";
        return token.substring(0, 5) + "..." + token.substring(token.length() - 5);
    }
    
    // Add CORS headers to ensure browser can read auth responses
    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        response.setHeader("Access-Control-Expose-Headers", "xsrf-token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}

