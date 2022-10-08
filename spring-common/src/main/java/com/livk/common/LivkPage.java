package com.livk.common;

import com.github.pagehelper.Page;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

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

    /**
     * {@link Page}
     *
     * @param list list or page
     */
    public LivkPage(List<T> list) {
        this(list, Function.identity());
    }

    public <R> LivkPage(List<R> list, Function<List<R>, List<T>> function) {
        this.list = function.apply(list);
        if (list instanceof Page<T> page) {
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
        } else {
            this.total = list.size();
        }
    }
}
