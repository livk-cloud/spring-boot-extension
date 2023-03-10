package com.livk.sso.auth.mapper;

import com.livk.sso.commons.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 */
@Mapper
public interface UserMapper {

    User getByUserName(@Param("userName") String userName);

}
