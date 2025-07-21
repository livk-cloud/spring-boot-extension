/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.export.mapper;

import com.livk.export.entity.Authors;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
				authors.setBirthdate(LocalDateTime.now());
				authors.setAdded(LocalDateTime.now());

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
