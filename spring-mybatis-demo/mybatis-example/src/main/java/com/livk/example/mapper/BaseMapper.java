package com.livk.example.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * BaseMapper
 * </p>
 *
 * @author livk
 * @date 2022/3/4
 */
public interface BaseMapper<T> {

	T selectById(@Param("id") Serializable id);

	int updateById(T t);

	int insert(T t);

	int deleteById(@Param("id") Serializable id);

	List<T> list();

}
