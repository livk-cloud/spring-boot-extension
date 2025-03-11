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

package com.livk.redisearch.webflux;

import com.livk.commons.util.BeanLambda;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.context.redisearch.StringRediSearchTemplate;
import com.livk.redisearch.webflux.entity.Student;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.Document;
import com.redis.lettucemod.search.Field;
import com.redis.lettucemod.search.SearchResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * RediSearchApp
 * </p>
 *
 * @author livk
 */
@Slf4j
@SpringBootApplication
public class RediSearchApp {

	public static void main(String[] args) {
		SpringApplication.run(RediSearchApp.class, args);
	}

	@Bean
	@SuppressWarnings("unchecked")
	public ApplicationRunner applicationRunner(StringRediSearchTemplate template) {
		return (args) -> {
			RedisModulesCommands<String, String> search = template.sync();

			if (!search.ftList().contains(Student.INDEX)) {
				search.ftCreate(Student.INDEX, Field.text(BeanLambda.fieldName(Student::getName)).weight(5.0).build(),
						Field.text(BeanLambda.fieldName(Student::getSex)).build(),
						Field.text(BeanLambda.fieldName(Student::getDesc)).build(), Field.tag("class").build());
			}
			ThreadLocalRandom random = ThreadLocalRandom.current();
			for (int i = 0; i < 10; i++) {
				int randomNum = random.nextInt(2);
				Student student = new Student().setName("livk-" + i)
					.setSex(randomNum == 0 ? "男" : "女")
					.setDesc("是一个学生")
					.setClassX((i + 1) + "班");
				Map<String, String> body = JsonMapperUtils.convertValueMap(student, String.class, String.class);
				search.hmset("00" + i, body);
			}
			SearchResults<String, String> result = search.ftSearch(Student.INDEX, "*");
			for (Document<String, String> document : result) {
				Student bean = JsonMapperUtils.convertValue(document, Student.class);
				log.info("{}", bean);
			}
		};
	}

}
