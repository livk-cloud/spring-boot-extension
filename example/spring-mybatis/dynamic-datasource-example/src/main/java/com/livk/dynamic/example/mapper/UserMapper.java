package com.livk.dynamic.example.mapper;

import com.livk.dynamic.example.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 */
@Mapper
public interface UserMapper {

    @Insert("insert into ${table} (username,password) values (#{user.username},#{user.password})")
    int insert(User user, String table);

    @Select("select * from ${table}")
    List<User> selectList(String table);
}
