package com.healthforu.disease.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "diseases")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Disease {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Indexed(unique = true)
    private String diseaseName;

    private String caution;

    private String etc;

    private String foodCategory;

    private String recommended;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId categoryId;

}
