package com.livk.graphql.mybatis.mapper;

import com.livk.graphql.mybatis.entity.Author;
import org.apache.ibatis.annotations.*;

/**
 * <p>
 * AuthorMapper
 * </p>
 *
 * @author livk
 */
@Mapper
public interface AuthorMapper {
    @Insert("insert into author(id_card_no, name, age) values (#{idCardNo},#{name},#{age})")
    int save(Author author);

    @Delete("truncate table author")
    void clear();

    @Select("select * from author where id_card_no = #{authorIdCardNo}")
    Author getByIdCardNo(@Param("authorIdCardNo") String authorIdCardNo);
}
