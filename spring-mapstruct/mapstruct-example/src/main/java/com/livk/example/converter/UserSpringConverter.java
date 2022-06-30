package com.livk.example.converter;

import com.livk.example.entity.User;
import com.livk.example.entity.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;

/**
 * <p>
 * UserSpringConverter
 * </p>
 *
 * @author livk
 * @date 2022/5/13
 */
@Mapper(componentModel = "spring")
public interface UserSpringConverter extends Converter<User, UserVO> {

    @Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "type", source = "type", numberFormat = "#")
    @Override
    UserVO convert(@Nonnull User user);

}
