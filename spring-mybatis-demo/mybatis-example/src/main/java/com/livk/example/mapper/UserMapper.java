package com.livk.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.livk.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    int UpdateByPrimaryKey(User user);
}
