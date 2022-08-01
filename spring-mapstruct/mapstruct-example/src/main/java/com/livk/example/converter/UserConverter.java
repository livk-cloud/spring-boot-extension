package com.livk.example.converter;

import com.livk.example.entity.User;
import com.livk.example.entity.UserVO;
import com.livk.mapstruct.converter.Converter;
import com.livk.util.DateUtils;
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

    @Mapping(target = "createTime", source = "createTime", dateFormat = DateUtils.YMD_HMS)
    @Mapping(target = "type", source = "type", numberFormat = "#")
    @Override
    User getSource(UserVO userVO);

    @Mapping(target = "createTime", source = "createTime", dateFormat = DateUtils.YMD_HMS)
    @Mapping(target = "type", source = "type", numberFormat = "#")
    @Override
    UserVO getTarget(User user);

}
