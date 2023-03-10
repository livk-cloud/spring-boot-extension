package com.livk.json.mapper;

import com.livk.json.entity.User;
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

    @Insert("insert into \"user\" (username,password,des) values (#{username},#{password},#{des})")
    int insert(User user);

    @Select("select * from \"user\"")
    List<User> selectList();
}
