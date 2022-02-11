package com.livk.excel.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>
 * ExcelInfo
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelInfo<T> {

    private final String ExcelName;

    private final List<T> data;

    public static <T> ExcelInfo<T> of(String excelName, List<T> data) {
        return new ExcelInfo<>(excelName, data);
    }
}
