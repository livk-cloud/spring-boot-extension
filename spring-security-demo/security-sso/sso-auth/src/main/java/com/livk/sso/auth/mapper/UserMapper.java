package com.livk.sso.auth.mapper;

import com.livk.sso.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@Mapper
public interface UserMapper {
    User getByUserName(@Param("userName") String userName);
}
