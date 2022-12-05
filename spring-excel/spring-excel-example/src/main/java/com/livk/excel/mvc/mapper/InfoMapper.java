package com.livk.excel.mvc.mapper;

import com.livk.excel.mvc.entity.Info;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * InfoMapper
 * </p>
 *
 * @author livk
 */
@Mapper
public interface InfoMapper {

    void insertBatch(List<Info> records);

}
