package com.livk.common;

import com.github.pagehelper.Page;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * LivkPage
 *
 * @author livk
 * @date 2021/8/19
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class LivkPage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final long total;

    private final List<T> list;

    private int pageNum;

    private int pageSize;

    private LivkPage(List<T> list) {
        this.list = list;
        if (list instanceof Page<T> page) {
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
        } else {
            this.total = list.size();
        }
    }

    private LivkPage(Page<T> page) {
        this.list = page.getResult();
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.total = page.getTotal();
    }

    public static <T> LivkPage<T> of(List<T> list) {
        return new LivkPage<>(list);
    }

    public static <T> LivkPage<T> of(Page<T> page) {
        return new LivkPage<>(page);
    }

}
