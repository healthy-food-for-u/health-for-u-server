package com.healthforu.user.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Indexed(unique = true)
    @Field("id")
    private String loginId;

    private String userName;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String mobile;

    @Builder
    public User(String loginId, String userName, String email, String mobile, String password){
        this.loginId=loginId;
        this.userName=userName;
        this.email=email;
        this.mobile=mobile;
    }
}
