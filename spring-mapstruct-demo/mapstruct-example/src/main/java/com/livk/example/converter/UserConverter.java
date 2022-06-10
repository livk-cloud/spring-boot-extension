package com.livk.example.converter;

import com.livk.example.entity.User;
import com.livk.example.entity.UserVO;
import com.livk.mapstruct.converter.Converter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * <p>
 * UserConverter
 * </p>
 *
 * @author livk
 * @date 2022/5/12
 */
@Mapper(componentModel = "spring")
public interface UserConverter extends Converter<User, UserVO> {

	@Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
	@Mapping(target = "type", source = "type", numberFormat = "#")
	@Override
	User getSource(UserVO userVO);

	@Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
	@Mapping(target = "type", source = "type", numberFormat = "#")
	@Override
	UserVO getTarget(User user);

}
