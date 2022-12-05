package com.livk.ck.mapper;

import com.livk.ck.entity.User;
import org.apache.ibatis.annotations.*;

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

    @Select("select id, app_id, version, reg_time from user")
    List<User> selectList();

    @Delete("alter table user delete where reg_time=#{regTime}")
    int delete(@Param("regTime") String regTime);

    @Insert("insert into user values (#{id},#{appId},#{version},#{regTime})")
    int insert(User user);
}
