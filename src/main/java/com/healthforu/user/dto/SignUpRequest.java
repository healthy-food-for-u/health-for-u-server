package com.healthforu.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{5,20}$",
            message="아이디는 5~20자의 영문, 숫자, 특수기호(_), (-)만 가능합니다.")
    private String loginId;

    @NotBlank(message = "이름을 입력하세요.")
    private String userName;

    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$",
            message = "올바른 이메일 형식이 아닙니다. (예: user@example.com)")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^01[016789][0-9]{7,8}$",
            message = "전화번호는 하이픈(-) 없이 숫자만 10~11자리로 입력해주세요.")
    private String mobile;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 16 ,message = "비밀번호는 4자에서 16자 사이로 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 위해 한 번 더 입력해주세요.")
    private String confirmPassword;

    public SignUpRequest(String loginId, String userName, String email, String mobile, String password, String confirmPassword){
        this.loginId=loginId;
        this.userName=userName;
        this.email=email;
        this.mobile=mobile;
        this.password=password;
        this.confirmPassword=confirmPassword;
    }
}
