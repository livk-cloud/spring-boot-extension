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
 * UserVOSpringConverter
 * </p>
 *
 * @author livk
 * @date 2022/9/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserVOSpringConverter extends Converter<UserVO, User> {


    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", source = "createTime", dateFormat = DateUtils.YMD_HMS)
    @Mapping(target = "type", source = "type", numberFormat = "#")
    @Override
    User convert(@Nullable UserVO source);
}
