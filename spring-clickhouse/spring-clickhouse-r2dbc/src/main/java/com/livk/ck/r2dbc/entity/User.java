package com.livk.ck.r2dbc.entity;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Data
@Accessors(chain = true)
@Table("user")
public class User {

    @Id
    private Integer id;

    private String appId;

    private String version;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date regTime;

    public static User collect(Row row, RowMetadata rowMetadata) {
        return new User();
    }
}
