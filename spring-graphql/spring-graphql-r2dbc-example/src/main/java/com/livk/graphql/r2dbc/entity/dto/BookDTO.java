package com.livk.graphql.r2dbc.entity.dto;

import lombok.Data;

/**
 * <p>
 * BookInput
 * </p>
 *
 * @author livk
 * @date 2022/11/24
 */
@Data
public class BookDTO {
    private String isbn;
    private String title;
    private Integer pages;
    private String authorIdCardNo;
}
