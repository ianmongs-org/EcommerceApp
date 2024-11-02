package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.LoginRequest;
import com.java.EcomerceApp.dto.MessageResponse;
import com.java.EcomerceApp.dto.SignupRequest;
import com.java.EcomerceApp.dto.UserInfoResponse;
import com.java.EcomerceApp.model.AppRole;
import com.java.EcomerceApp.model.Role;
import com.java.EcomerceApp.model.User;
import com.java.EcomerceApp.repository.RoleRepository;
import com.java.EcomerceApp.repository.UserRepository;
import com.java.EcomerceApp.security.services.AuthService;
import com.java.EcomerceApp.security.services.UserDetailsImpl;
import com.java.EcomerceApp.security.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
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
