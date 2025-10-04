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

package com.livk.redisearch.mvc.controller;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.context.redisearch.StringRediSearchTemplate;
import com.livk.redisearch.mvc.entity.Student;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import io.lettuce.core.search.SearchReply;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author livk
 */
@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentController {

	private final StringRediSearchTemplate template;

	@GetMapping
	public HttpEntity<List<Student>> list(@RequestParam(defaultValue = "*") String query) {
		RedisModulesCommands<String, String> search = template.sync();
		SearchReply<String, String> result = search.ftSearch(Student.INDEX, query);
		List<Student> studentList = result.getResults()
			.stream()
			.map(searchResult -> JsonMapperUtils.convertValue(searchResult.getFields(), Student.class))
			.toList();
		return ResponseEntity.ok(studentList);
	}

}
