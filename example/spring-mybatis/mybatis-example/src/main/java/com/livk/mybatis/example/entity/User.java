package com.livk.mybatis.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.livk.autoconfigure.mybatis.inject.annotation.SqlFunction;
import com.livk.autoconfigure.mybatis.inject.enums.FunctionType;
import com.livk.autoconfigure.mybatis.inject.enums.SqlFill;
import com.livk.commons.util.DateUtils;
import com.livk.mybatis.example.handler.VersionFunction;
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

    @JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
    @SqlFunction(fill = SqlFill.INSERT, time = FunctionType.DATE)
    private Date insertTime;

    @JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
    @SqlFunction(fill = SqlFill.INSERT_UPDATE, time = FunctionType.DATE)
    private Date updateTime;
}
