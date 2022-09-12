package com.livk.example.converter;

import com.livk.example.entity.User;
import com.livk.example.entity.UserVO;
import com.livk.util.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

/**
 * <p>
 * UserVOSpringConverter
 * </p>
 *
 * @author livk
 * @date 2022/9/12
 */
@Mapper(componentModel = "spring")
public interface UserVOSpringConverter extends Converter<UserVO, User> {

    @Mapping(target = "createTime", source = "createTime", dateFormat = DateUtils.YMD_HMS)
    @Mapping(target = "type", source = "type", numberFormat = "#")
    @Override
    User convert(UserVO source);
}
