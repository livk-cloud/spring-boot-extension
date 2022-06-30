package com.livk.json.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.livk.json.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 * @date 2022/5/26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
