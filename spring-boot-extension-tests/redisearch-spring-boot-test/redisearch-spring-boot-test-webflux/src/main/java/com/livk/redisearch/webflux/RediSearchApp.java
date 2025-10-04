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

package com.livk.redisearch.webflux;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.util.BeanLambda;
import com.livk.context.redisearch.StringRediSearchTemplate;
import com.livk.redisearch.webflux.entity.Student;
import com.redis.lettucemod.api.reactive.RedisModulesReactiveCommands;
import io.lettuce.core.search.SearchReply;
import io.lettuce.core.search.arguments.TagFieldArgs;
import io.lettuce.core.search.arguments.TextFieldArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	public ApplicationRunner applicationRunner(StringRediSearchTemplate template) {
		return (args) -> {
			RedisModulesReactiveCommands<String, String> reactive = template.reactive();

			Mono<Void> createIndex = reactive.ftList()
				.any(index -> index.equals(Student.INDEX))
				.filter(exists -> !exists)
				.flatMap(exist -> {
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
					return reactive.ftCreate(Student.INDEX, List.of(nameArg, sexArg, descArg, tagArg)).then();
				});
			ThreadLocalRandom random = ThreadLocalRandom.current();
			Flux<Student> studentFlux = Flux.range(0, 10).map(i -> {
				int randomNum = random.nextInt(2);
				return new Student().setName("livk-" + i)
					.setSex(randomNum == 0 ? "男" : "女")
					.setDesc("是一个学生")
					.setClassX((i + 1) + "班");
			});

			Flux<String> insertData = studentFlux.flatMap(student -> {
				Map<String, String> body = JsonMapperUtils.convertValueMap(student, String.class, String.class);
				String key = "00" + student.getName().replace("livk-", "");
				return reactive.hmset(key, body);
			});

			Mono<SearchReply<String, String>> searchResult = reactive.ftSearch(Student.INDEX, "*");

			createIndex.thenMany(insertData)
				.then(searchResult)
				.map(SearchReply::getResults)
				.flatMapMany(Flux::fromIterable)
				.map(result -> JsonMapperUtils.convertValue(result.getFields(), Student.class))
				.doOnNext(student -> log.info("{}", student))
				.subscribe();
		};
	}

}
