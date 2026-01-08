package com.healthforu.user.service.impl;

import com.healthforu.common.exception.custom.*;
import com.healthforu.user.domain.User;
import com.healthforu.user.dto.LoginRequest;
import com.healthforu.user.dto.SignUpRequest;
import com.healthforu.user.dto.UserResponse;
import com.healthforu.user.repository.UserRepository;
import com.healthforu.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse signUp(SignUpRequest request) {
        validateDuplicateUser(request);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordNotMatchException();
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User(
                request.getLoginId(),
                request.getUserName(),
                request.getEmail(),
                request.getMobile(),
                encodedPassword
        );

        User savedUser = userRepository.save(newUser);

        return UserResponse.from(savedUser);
    }

    @Override
    public UserResponse login(LoginRequest request, HttpServletRequest httpServletRequest) {

        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new UserNotFoundException());

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new PasswordNotMatchException();
        }

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("LOGIN_USER", user.getLoginId());

        return UserResponse.from(user);
    }

    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private void validateDuplicateUser(SignUpRequest request) {
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new DuplicateIdException();
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }
        if (userRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateMobileException();
        }
    }
}
