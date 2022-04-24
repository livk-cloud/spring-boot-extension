package com.livk.ck.mapper;

import com.livk.ck.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
@Mapper
public interface UserMapper {

    @Select("select id, app_id, version, reg_time from user")
    List<User> list();

    @Delete("alter table user delete where reg_time=#{regTime}")
    int delete(@Param("regTime") String regTime);
}
