package com.livk.export.mapper;

import com.livk.export.entity.Authors;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author livk
 */
@Mapper
public interface AuthorsMapper {

    List<Authors> select();

    void insertBatch(List<Authors> authorsList);
}
