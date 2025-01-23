package com.java.EcomerceApp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.EcomerceApp.dto.LoginRequest;
import com.java.EcomerceApp.dto.SignupRequest;
import com.java.EcomerceApp.dto.UserInfoResponse;
import com.java.EcomerceApp.security.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    @Operation(summary = "Sign in", description = "Authenticate user and return user information")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        try {
            UserInfoResponse loginResponse = authService.authenticateUser(loginRequest);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up", description = "Register a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            return new ResponseEntity<>(authService.registerUser(signUpRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}