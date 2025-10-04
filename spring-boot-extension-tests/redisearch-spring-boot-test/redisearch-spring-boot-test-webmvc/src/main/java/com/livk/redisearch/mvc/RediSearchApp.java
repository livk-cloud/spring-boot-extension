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

package com.livk.redisearch.mvc;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.util.BeanLambda;
import com.livk.context.redisearch.StringRediSearchTemplate;
import com.livk.redisearch.mvc.entity.Student;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import io.lettuce.core.search.SearchReply;
import io.lettuce.core.search.arguments.TagFieldArgs;
import io.lettuce.core.search.arguments.TextFieldArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
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
				TextFieldArgs<String> nameArg = TextFieldArgs.<String>builder()
					.name(BeanLambda.fieldName(Student::getName))
					.weight(5)
					.build();
				TextFieldArgs<String> sexArg = TextFieldArgs.<String>builder()
					.name(BeanLambda.fieldName(Student::getSex))
					.build();
				TextFieldArgs<String> descArg = TextFieldArgs.<String>builder()
					.name(BeanLambda.fieldName(Student::getDesc))
					.build();
				TagFieldArgs<String> tagArg = TagFieldArgs.<String>builder().name("class").build();
				search.ftCreate(Student.INDEX, List.of(nameArg, sexArg, descArg, tagArg));
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
			SearchReply<String, String> result = search.ftSearch(Student.INDEX, "*");
			for (SearchReply.SearchResult<String, String> resultResult : result.getResults()) {
				Student bean = JsonMapperUtils.convertValue(resultResult.getFields(), Student.class);
				log.info("{}", bean);
			}
		};
	}

}
