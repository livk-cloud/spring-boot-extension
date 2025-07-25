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

package com.livk.excel.batch.config;

import com.livk.excel.batch.entity.Info;
import com.livk.excel.batch.support.MyBatisBatchItemWriter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author livk
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public ItemWriter<List<Info>> writer(SqlSessionFactory sqlSessionFactory) {
		return new MyBatisBatchItemWriter<>(sqlSessionFactory, "com.livk.excel.batch.mapper.InfoMapper.saveBatch");
	}

}
