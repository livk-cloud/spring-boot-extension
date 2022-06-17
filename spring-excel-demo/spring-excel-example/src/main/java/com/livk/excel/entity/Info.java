package com.livk.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * Info
 * </p>
 *
 * @author livk
 * @date 2022/1/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelIgnore
    private Long id;

    @ExcelProperty(index = 0)
    private String phone;

}
