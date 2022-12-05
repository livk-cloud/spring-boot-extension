package com.livk.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.livk.autoconfigure.mybatis.annotation.SqlFunction;
import com.livk.autoconfigure.mybatis.constant.FunctionEnum;
import com.livk.autoconfigure.mybatis.enums.SqlFill;
import com.livk.commons.util.DateUtils;
import com.livk.example.handler.VersionFunction;
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
    @SqlFunction(fill = SqlFill.INSERT, time = FunctionEnum.DATE)
    private Date insertTime;

    @JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
    @SqlFunction(fill = SqlFill.INSERT_UPDATE, time = FunctionEnum.DATE)
    private Date updateTime;
}
