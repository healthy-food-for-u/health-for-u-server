package com.healthforu.user.service;

import com.healthforu.user.dto.LoginRequest;
import com.healthforu.user.dto.SignUpRequest;
import com.healthforu.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserResponse signUp(SignUpRequest request);

    UserResponse login(LoginRequest request, HttpServletRequest httpServletRequest);

    void logout(HttpServletRequest request);

    boolean checkLoginIdDuplicate(String loginId);

    UserResponse getUserByLoginId(String loginId);
}
