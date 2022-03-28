package com.livk.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.livk.annotation.SqlFunction;
import com.livk.enums.SqlFill;
import com.livk.example.handler.VersionFunction;
import com.livk.handler.DateFunction;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    @JsonIgnore
    @SqlFunction(fill = SqlFill.INSERT, supplier = VersionFunction.class)
    private Integer version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @SqlFunction(fill = SqlFill.INSERT, supplier = DateFunction.class)
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @SqlFunction(fill = SqlFill.INSERT_UPDATE, supplier = DateFunction.class)
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
