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

package com.livk.excel.batch.support;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.jspecify.annotations.NonNull;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class MyBatisBatchItemWriter<T> implements ItemWriter<T> {

	private final SqlSessionFactory sqlSessionFactory;

	private final String statementId;

	@Override
	public void write(@NonNull Chunk<? extends T> chunk) {
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		try {
			for (T item : chunk.getItems()) {
				sqlSession.update(statementId, item);
			}
			sqlSession.commit();
		}
		catch (Exception e) {
			sqlSession.rollback();
		}
		sqlSession.close();
	}

}
