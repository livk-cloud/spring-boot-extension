package com.livk.graphql.mybatis.entity.dto;

import lombok.Data;

/**
 * <p>
 * AuthorInput
 * </p>
 *
 * @author livk
 * @date 2022/11/24
 */
@Data
public class AuthorDTO {
    private String idCardNo;
    private String name;
    private Integer age;
}
