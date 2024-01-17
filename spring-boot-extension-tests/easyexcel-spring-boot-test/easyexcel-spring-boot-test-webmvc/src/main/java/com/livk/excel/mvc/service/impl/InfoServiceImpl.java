/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.excel.mvc.service.impl;

import com.google.common.collect.Lists;
import com.livk.excel.mvc.entity.Info;
import com.livk.excel.mvc.mapper.InfoMapper;
import com.livk.excel.mvc.service.InfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * <p>
 * InfoServiceImpl
 * </p>
 *
 * @author livk
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

	private final SqlSessionFactory sqlSessionFactory;

	@Override
	public void insertBatch(List<Info> records) {
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		InfoMapper infoMapper = sqlSession.getMapper(InfoMapper.class);
		List<List<Info>> partition = Lists.partition(records, 1000);
		int i = 0;
		for (List<Info> infos : partition) {
			try {
				infoMapper.insertBatch(infos);
			}
			catch (Exception e) {
				log.error("msg:{}", e.getMessage(), e);
				sqlSession.rollback();
				break;
			}
			i++;
			if (i % 1000 == 0) {
				sqlSession.commit();
				sqlSession.clearCache();
			}
		}
		sqlSession.commit();
		sqlSession.clearCache();
		sqlSession.close();
	}

	public void insertBatchMultithreading(List<Info> records) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>());
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		InfoMapper infoMapper = sqlSession.getMapper(InfoMapper.class);
		List<List<Info>> partition = Lists.partition(records, (int) Math.pow(1000, 2));
		List<Callable<Integer>> callables = partition.stream().map((Function<List<Info>, Callable<Integer>>) infos -> {
			List<List<Info>> lists = Lists.partition(infos, 1000);
			for (List<Info> list : lists) {
				try {
					infoMapper.insertBatch(list);
				}
				catch (Exception e) {
					return () -> 0;
				}
			}
			return () -> 1;
		}).toList();
		try {
			List<Future<Integer>> futures = executor.invokeAll(callables);
			for (Future<Integer> future : futures) {
				if (future.get() <= 0) {
					sqlSession.rollback();
					break;
				}
			}
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		sqlSession.commit();
		sqlSession.clearCache();
		sqlSession.close();
	}

}
