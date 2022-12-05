package com.livk.graphql.r2dbc.entity.dto;

import lombok.Data;

/**
 * <p>
 * BookInput
 * </p>
 *
 * @author livk
 */
@Data
public class BookDTO {
    private String isbn;
    private String title;
    private Integer pages;
    private String authorIdCardNo;
}
