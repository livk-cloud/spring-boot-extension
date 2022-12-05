package com.livk.example.mapper;

import com.livk.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
