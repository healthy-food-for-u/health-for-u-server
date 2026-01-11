package com.healthforu.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id // MongoDB의 _id 필드와 매핑됩니다.
    private String id;

    @Indexed(unique = true)
    @Field("id")
    private String loginId;

    private String userName;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
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
