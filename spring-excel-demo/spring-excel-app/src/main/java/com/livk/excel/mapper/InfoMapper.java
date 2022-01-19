package com.livk.excel.mapper;

import com.livk.excel.entity.Info;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * InfoMapper
 * </p>
 *
 * @author livk
 * @date 2022/1/12
 */
@Mapper
public interface InfoMapper {

    void insertBatch(List<Info> records);
}
