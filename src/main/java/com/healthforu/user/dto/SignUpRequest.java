package com.healthforu.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    private String loginId;
    private String userName;
    private String email;
    private String mobile;
    private String password;

    public SignUpRequest(String loginId, String userName, String email, String mobile, String password){
        this.loginId=loginId;
        this.userName=userName;
        this.email=email;
        this.mobile=mobile;
        this.password=password;
    }
}
