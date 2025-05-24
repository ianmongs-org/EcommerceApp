package com.java.EcomerceApp.security.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.EcomerceApp.dto.LoginRequest;
import com.java.EcomerceApp.dto.SignupRequest;
import com.java.EcomerceApp.dto.UserInfoResponse;
import com.java.EcomerceApp.model.AppRole;
import com.java.EcomerceApp.model.Role;
import com.java.EcomerceApp.model.User;
import com.java.EcomerceApp.repository.RoleRepository;
import com.java.EcomerceApp.repository.UserRepository;
import com.java.EcomerceApp.security.utils.JwtUtils;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public UserInfoResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        String jwt = jwtUtils.generateTokenFromUsername(userDetails);
        
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        return new UserInfoResponse(
                userDetails.getId(),
                jwt,
                userDetails.getUsername(),
                roles);
    }

    public Map<String, Object> registerUser(SignupRequest signupRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            logger.info("Processing registration for username: {}", signupRequest.getUsername());
            
            // Check if username already exists
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                logger.warn("Registration failed: Username {} is already taken", signupRequest.getUsername());
                response.put("status", false);
                response.put("message", "Username is already taken!");
                return response;
            }

            // Check if email already exists
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                logger.warn("Registration failed: Email {} is already in use", signupRequest.getEmail());
                response.put("status", false);
                response.put("message", "Email is already in use!");
                return response;
            }

            // Create and save new user
            User user = new User(
                    signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    encoder.encode(signupRequest.getPassword()));

            // Set default role as USER if roles not specified
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByAppRole(AppRole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER not found."));
            roles.add(userRole);
            
            // Add ADMIN role if specified in request
            if (signupRequest.getRole() != null && signupRequest.getRole().contains("admin")) {
                Role adminRole = roleRepository.findByAppRole(AppRole.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found."));
                roles.add(adminRole);
            }
            
            user.setRoles(roles);
            userRepository.save(user);
            
            logger.info("User registered successfully: {}", user.getUsername());
            
            // Return success response without token
            response.put("status", true);
            response.put("message", "User registered successfully!");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            
            return response;
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            response.put("status", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return response;
        }
    }
}
