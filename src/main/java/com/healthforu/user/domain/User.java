package com.healthforu.user.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@Getter
public class User {

    @Id // MongoDB의 _id 필드와 매핑됩니다.
    private String id;

    @Field("id")
    private String loginId;

    private String userName;

    private String email;

    private String mobile;

    private String password; // 암호화된 비밀번호 ($2b$10... 형식이니 그대로 String)

    @Builder
    public User(String loginId, String userName, String email, String mobile, String password){
        this.loginId=loginId;
        this.userName=userName;
        this.email=email;
        this.mobile=mobile;
        this.password=password;
    }
}
