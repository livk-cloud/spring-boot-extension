package com.livk.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.livk.annotation.SqlFunction;
import com.livk.enums.SqlFill;
import com.livk.example.handler.VersionFunction;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "`user`")
public class User extends BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "username")
    private String username;

    @SqlFunction(fill = SqlFill.INSERT, supplier = VersionFunction.class)
    @TableField(value = "version")
    private Integer version;

    @Serial
    private static final long serialVersionUID = 1L;
}
