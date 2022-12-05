package com.livk.mybatis.tree.example.mapper;

import com.livk.mybatis.tree.example.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * MenuMapper
 * </p>
 *
 * @author livk
 */
@Mapper
public interface MenuMapper {

    List<Menu> list();
}
