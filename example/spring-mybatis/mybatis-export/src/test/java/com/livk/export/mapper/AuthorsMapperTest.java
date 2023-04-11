package com.livk.export.mapper;

import com.livk.export.entity.Authors;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author livk
 */
@Disabled("测试数据生产")
@SpringBootTest
class AuthorsMapperTest {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Test
    void insert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            AuthorsMapper mapper = sqlSession.getMapper(AuthorsMapper.class);

            List<Authors> authorsList = new ArrayList<>();
            for (int i = 0; i < 2000000; i++) {
                Authors authors = new Authors();
                authors.setFirstName("root" + i);
                authors.setLastName("admin" + i);
                authors.setEmail("1375632510@qq.com");
                authors.setBirthdate(new Date());
                authors.setAdded(new Date());

                authorsList.add(authors);

                if (i > 0 && i % 1000 == 0) {
                    mapper.insertBatch(authorsList);
                    authorsList.clear();
                }
                if (i > 0 && i % 1000000 == 0) {
                    sqlSession.commit();
                    sqlSession.clearCache();
                }
            }

            sqlSession.commit();
        }
    }
}
