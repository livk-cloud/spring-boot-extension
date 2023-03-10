package com.livk.example.converter;

import com.livk.commons.util.DateUtils;
import com.livk.example.entity.User;
import com.livk.example.entity.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

/**
 * <p>
 * UserSpringConverter
 * </p>
 *
 * @author livk
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserSpringConverter extends Converter<User, UserVO> {

    @Mapping(target = "createTime", source = "createTime", dateFormat = DateUtils.YMD_HMS)
    @Mapping(target = "type", source = "type", numberFormat = "#")
    @Override
    UserVO convert(@Nullable User user);

}
