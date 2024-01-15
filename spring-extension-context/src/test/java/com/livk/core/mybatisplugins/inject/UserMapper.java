package com.livk.core.mybatisplugins.inject;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author livk
 */
@Mapper
interface UserMapper {

	@Select("select * from `user` where id = #{id}")
	User getById(@Param("id") Integer id);

	@Insert("insert into `user`(username, insert_time, update_time) "
			+ "values(#{user.username}, #{user.insertTime}, #{user.updateTime})")
	void insert(@Param("user") User user);

}
