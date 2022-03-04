package com.livk.example.mapper;

import com.livk.example.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@Mapper
public interface UserMapper {
    User selectById(@Param("id") Integer id);

    int updateById(User user);

    int insert(User user);

    int deleteById(@Param("id") Integer id);

    @Select("SELECT id, username, version, insert_time, update_time FROM user ORDER BY insert_time DESC")
    @ResultMap("BaseResultMap")
    @Results(id = "BaseResultMap", value = {
            @Result(id = true),
            @Result(column = "username", property = "username"),
            @Result(column = "version", property = "version"),
            @Result(column = "insert_time", property = "insertTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<User> list();
}
