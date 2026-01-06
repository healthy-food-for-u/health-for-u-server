package com.healthforu.user.service;

import com.healthforu.user.dto.SignUpRequest;
import com.healthforu.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserResponse signUp(SignUpRequest request);

    UserResponse login(String loginId, String password);

    void logout(HttpServletRequest request);
}
