package com.livk.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.livk.auth.domain.Users;
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
public interface UsersMapper extends BaseMapper<Users> {

}
