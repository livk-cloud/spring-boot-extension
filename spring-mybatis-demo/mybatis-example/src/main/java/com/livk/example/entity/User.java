package com.livk.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.livk.annotation.SqlFunction;
import com.livk.constant.FunctionEnum;
import com.livk.enums.SqlFill;
import com.livk.example.handler.VersionFunction;
import com.livk.function.FieldFunction;
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

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    @JsonIgnore
    @SqlFunction(fill = SqlFill.INSERT, supplier = VersionFunction.class)
    private Integer version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @SqlFunction(fill = SqlFill.INSERT, time = FunctionEnum.DATE)
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @SqlFunction(fill = SqlFill.INSERT_UPDATE, time = FunctionEnum.DATE)
    private Date updateTime;

    public static <T> String of(FieldFunction<T> function) {
        return function.getFieldName();
    }

}
