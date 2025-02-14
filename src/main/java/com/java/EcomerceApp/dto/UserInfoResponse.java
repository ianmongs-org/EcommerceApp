package com.java.EcomerceApp.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserInfoResponse {
    private Long id;
    private String jwtToken;

    private String username;
    private List<String> roles;


}


