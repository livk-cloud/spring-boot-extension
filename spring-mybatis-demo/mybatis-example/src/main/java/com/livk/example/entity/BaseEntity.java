package com.livk.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.livk.annotation.SqlFunction;
import com.livk.enums.SqlFill;
import com.livk.handler.DateFaction;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * BaseEntity
 * </p>
 *
 * @author livk
 * @date 2022/3/4
 */
@Data
public class BaseEntity {
    @SqlFunction(fill = SqlFill.INSERT, supplier = DateFaction.class)
    @TableField(value = "insert_time")
    private Date insertTime;

    @SqlFunction(fill = SqlFill.INSERT_UPDATE, supplier = DateFaction.class)
    @TableField(value = "update_time")
    private Date updateTime;
}
