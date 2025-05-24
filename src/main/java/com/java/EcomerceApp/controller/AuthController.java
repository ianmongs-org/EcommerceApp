package com.java.EcomerceApp.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.EcomerceApp.dto.LoginRequest;
import com.java.EcomerceApp.dto.SignupRequest;
import com.java.EcomerceApp.dto.UserInfoResponse;
import com.java.EcomerceApp.security.services.AuthService;
import com.java.EcomerceApp.security.utils.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs for login and registration")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    @Operation(summary = "User login", description = "Authenticate user and return token")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for user: {}", loginRequest.getUsername());
            
            // Validate request
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                logger.warn("Login attempt with empty username");
                return ResponseEntity.badRequest().body(createErrorResponse("Username cannot be empty"));
            }
            
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                logger.warn("Login attempt with empty password for user: {}", loginRequest.getUsername());
                return ResponseEntity.badRequest().body(createErrorResponse("Password cannot be empty"));
            }
            
            // Attempt authentication
            UserInfoResponse userInfo = authService.authenticateUser(loginRequest);
            
            // Log success and token info (masked)
            logger.info("Login successful for user: {}", userInfo.getUsername());
            
            // For debugging only - check token structure
            if (userInfo.getJwtToken() != null) {
                jwtUtils.printTokenDiagnostics(userInfo.getJwtToken());
            }
            
            return ResponseEntity.ok(userInfo);
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for user: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(createErrorResponse("Invalid username or password"));
        } catch (AuthenticationException e) {
            logger.error("Authentication error for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(createErrorResponse("Authentication failed: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during login for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(createErrorResponse("An error occurred during login"));
        }
    }

    @PostMapping("/signup")
    @Operation(summary = "Register user", description = "Register new user and return token")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            logger.info("Registration attempt for user: {}", signupRequest.getUsername());
            
            // Validate request
            if (signupRequest.getUsername() == null || signupRequest.getUsername().trim().isEmpty()) {
                logger.warn("Registration attempt with empty username");
                return ResponseEntity.badRequest().body(createErrorResponse("Username cannot be empty"));
            }
            
            if (signupRequest.getEmail() == null || signupRequest.getEmail().trim().isEmpty()) {
                logger.warn("Registration attempt with empty email for user: {}", signupRequest.getUsername());
                return ResponseEntity.badRequest().body(createErrorResponse("Email cannot be empty"));
            }
            
            if (signupRequest.getPassword() == null || signupRequest.getPassword().trim().isEmpty()) {
                logger.warn("Registration attempt with empty password for user: {}", signupRequest.getUsername());
                return ResponseEntity.badRequest().body(createErrorResponse("Password cannot be empty"));
            }
            
            // Register user
            Map<String, Object> result = authService.registerUser(signupRequest);
            
            // Check if registration was successful
            if ((Boolean) result.get("status")) {
                logger.info("Registration successful for user: {}", signupRequest.getUsername());
                return ResponseEntity.ok(result);
            } else {
                logger.warn("Registration failed for user {}: {}", 
                          signupRequest.getUsername(), result.get("message"));
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.error("Unexpected error during registration for user {}: {}", 
                       signupRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body(createErrorResponse("An error occurred during registration"));
        }
    }
    
    // Helper method to create error response
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", false);
        return response;
    }
}