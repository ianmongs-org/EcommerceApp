package com.java.EcomerceApp.security.utils;

import com.java.EcomerceApp.model.User;
import com.java.EcomerceApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil{
    @Autowired
    private UserRepository userRepository;

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->new UsernameNotFoundException("User not found"));
        return user;
    }
    public String loggedInUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->new UsernameNotFoundException("User not found"));
        return user.getEmail();
    }
}
