package com.livk.graphql.mybatis.entity;

import lombok.Data;

/**
 * <p>
 * Book
 * </p>
 *
 * @author livk
 */
@Data
public class Book {
    private Long id;
    private String isbn;
    private String title;
    private Integer pages;
    private String authorIdCardNo;
}
