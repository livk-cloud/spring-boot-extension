package com.livk.commons.domain;

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
 * @param <T> the type parameter
 * @author livk
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class LivkPage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 总数
     */
    private final long total;
    /**
     * 当前分页数据
     */
    private final List<T> list;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 数量
     */
    private int pageSize;

    /**
     * {@link Page}
     *
     * @param list list or page
     */
    public LivkPage(List<T> list) {
        this(list, Function.identity());
    }

    /**
     * 构建分页实体，同时使用{@link Function}转换list
     *
     * @param list     the list
     * @param function the function
     * @param <R>      the r
     */
    public <R> LivkPage(List<R> list, Function<List<R>, List<T>> function) {
        if (list instanceof Page<R> page) {
            this.list = function.apply(page.getResult());
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
        } else {
            this.total = list.size();
            this.list = function.apply(list);
        }
    }
}
