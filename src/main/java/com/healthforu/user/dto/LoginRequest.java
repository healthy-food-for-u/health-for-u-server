package com.healthforu.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String loginId;
    private String password;

    public LoginRequest(String loginId, String password){
        this.loginId=loginId;
        this.password=password;
    }
}
