package com.livk.graphql.mybatis.mapper;

import com.livk.graphql.mybatis.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * BookMapper
 * </p>
 *
 * @author livk
 * @date 2022/11/25
 */
@Mapper
public interface BookMapper {
    @Select("select * from book")
    List<Book> list();

    @Select("select * from book where isbn = #{isbn}")
    Book getByIsbn(@Param("isbn") String isbn);

    @Delete("truncate table book")
    void clear();

    @Insert("insert into book(isbn, title, pages, author_id_card_no) values (#{isbn},#{title},#{pages},#{authorIdCardNo})")
    int save(Book book);
}
