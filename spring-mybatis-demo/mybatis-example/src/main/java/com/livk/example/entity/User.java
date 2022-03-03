package com.livk.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.livk.annotation.SqlFunction;
import com.livk.enums.SqlFill;
import com.livk.example.handler.VersionFunction;
import com.livk.handler.DateFaction;
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
@TableName(value = "`user`")
public class User implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "username")
    private String username;

    @SqlFunction(fill = SqlFill.INSERT, supplier = VersionFunction.class)
    @TableField(value = "version")
    private Integer version;

    @SqlFunction(fill = SqlFill.INSERT, supplier = DateFaction.class)
    @TableField(value = "insert_time")
    private Date insertTime;

    @SqlFunction(fill = SqlFill.INSERT_UPDATE, supplier = DateFaction.class)
    @TableField(value = "update_time")
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
