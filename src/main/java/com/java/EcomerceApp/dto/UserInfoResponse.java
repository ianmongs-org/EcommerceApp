package com.java.EcomerceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoResponse {
    private Long id;
    private String jwtToken;

    private String username;
    private List<String> roles;


}


