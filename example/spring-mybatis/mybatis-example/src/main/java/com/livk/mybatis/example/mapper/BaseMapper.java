package com.livk.mybatis.example.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * BaseMapper
 * </p>
 *
 * @author livk
 */
public interface BaseMapper<T> {

    T selectById(@Param("id") Serializable id);

    int updateById(T t);

    int insert(T t);

    int deleteById(@Param("id") Serializable id);

    List<T> list();

}
