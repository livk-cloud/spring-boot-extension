package com.livk.graphql.r2dbc.entity;

import lombok.Data;

/**
 * <p>
 * Author
 * </p>
 *
 * @author livk
 */
@Data
public class Author {
    private Long id;
    private String idCardNo;
    private String name;
    private Integer age;
}
