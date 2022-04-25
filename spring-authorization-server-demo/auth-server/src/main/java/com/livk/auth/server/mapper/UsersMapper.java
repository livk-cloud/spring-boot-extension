package com.livk.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.livk.auth.server.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * UsersMapper
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
@Mapper
public interface UsersMapper extends BaseMapper<User> {

}
