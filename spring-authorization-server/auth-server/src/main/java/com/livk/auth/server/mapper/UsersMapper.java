package com.livk.auth.server.mapper;

import com.livk.auth.server.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * UsersMapper
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
@Mapper
public interface UsersMapper {

    @Select("select * from users")
    User getOne(@Param("username") String username);
}
