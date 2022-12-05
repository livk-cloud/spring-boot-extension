package com.livk.graphql.r2dbc.entity.dto;

import lombok.Data;

/**
 * <p>
 * AuthorInput
 * </p>
 *
 * @author livk
 */
@Data
public class AuthorDTO {
    private String idCardNo;
    private String name;
    private Integer age;
}
