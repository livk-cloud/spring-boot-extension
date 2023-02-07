package com.livk.redisearch.webflux.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * Student
 * </p>
 *
 * @author livk
 * @date 2023/2/3
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Student {

    public static final String INDEX = "student";

    @JsonProperty("name")
    private String name;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("class")
    private String classX;
}
