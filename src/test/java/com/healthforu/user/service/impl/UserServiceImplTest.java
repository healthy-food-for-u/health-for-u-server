package com.healthforu.user.service.impl;

import com.healthforu.common.exception.BusinessException;
import com.healthforu.common.exception.custom.DuplicateIdException;
import com.healthforu.common.exception.custom.PasswordNotMatchException;
import com.healthforu.common.exception.custom.UserNotFoundException;
import com.healthforu.user.domain.User;
import com.healthforu.user.dto.LoginRequest;
import com.healthforu.user.dto.SignUpRequest;
import com.healthforu.user.dto.UserResponse;
import com.healthforu.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() {
        SignUpRequest request = new SignUpRequest("testId", "testName", "test@email.com", "010-1111-2222", "123#", "123#");

        User mockUser = User.builder()
                .loginId("testId")
                .userName("testName")
                .email("test@email.com")
                .mobile("010-1111-2222")
                .password("123#")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        when(passwordEncoder.encode(any())).thenReturn("encoded_123#");

        UserResponse response = userService.signUp(request);

        assertNotNull(response);
        assertEquals("testId", response.loginId());
        assertEquals("testName", response.userName());

        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void signUp_Failed_Duplicate_LoginId() {
        SignUpRequest request = new SignUpRequest("testId", "testName", "test@email.com", "010-1111-2222", "123#", "123#");

        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(true);

        BusinessException exception = assertThrows(DuplicateIdException.class, () -> {
            userService.signUp(request);
        });

        assertEquals("이미 사용 중인 아이디입니다. 다른 아이디를 입력해 주세요.", exception.getMessage());

    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        LoginRequest loginRequest = new LoginRequest("testId", "123#");

        User mockUser = User.builder()
                .loginId("testId")
                .password("encoded_123#")
                .build();

        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(httpServletRequest.getSession(true)).thenReturn(session);


        userService.login(loginRequest, httpServletRequest);

        verify(session, times(1)).setAttribute(eq("LOGIN_USER"), any());
        verify(userRepository, times(1)).findByLoginId(anyString());

    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Failed_Wrong_Password() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        LoginRequest loginRequest = new LoginRequest("testId", "123#");

        User mockUser = User.builder()
                .loginId("testId")
                .password("encoded_123#")
                .build();

        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);


        BusinessException exception = assertThrows(PasswordNotMatchException.class, ()->{
            userService.login(loginRequest, httpServletRequest);
        });

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_Failed_User_Not_Found() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        LoginRequest loginRequest = new LoginRequest("testId", "123#");

        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(UserNotFoundException.class, ()->{
            userService.login(loginRequest, httpServletRequest);
        });

        assertEquals("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그아웃 성공 - 세션이 존재할 때 무효화")
    void logout_Success() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);

        userService.logout(request);

        verify(session, times(1)).invalidate();
    }

    @Test
    @DisplayName("로그아웃 - 세션이 이미 없는 경우 아무 일도 일어나지 않음")
    void logout_NoSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        assertDoesNotThrow(() -> userService.logout(request));
    }

}