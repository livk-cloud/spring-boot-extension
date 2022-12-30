package com.livk.redis.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * <p>
 * Person
 * </p>
 *
 * @author livk
 * @date 2022/12/30
 */
@Data
@RedisHash("people")
public class Person {
    @Id
    private String id;
    private String username;
    private String address;
}
