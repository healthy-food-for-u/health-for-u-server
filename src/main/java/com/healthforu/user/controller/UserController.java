package com.healthforu.user.controller;

import com.healthforu.user.dto.LoginRequest;
import com.healthforu.user.dto.SignUpRequest;
import com.healthforu.user.dto.UserResponse;
import com.healthforu.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request
                                            ,HttpServletRequest httpServletRequest) {
        UserResponse response = userService.login(request, httpServletRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam("id") String loginId) {
        boolean exists = userService.checkLoginIdDuplicate(loginId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
