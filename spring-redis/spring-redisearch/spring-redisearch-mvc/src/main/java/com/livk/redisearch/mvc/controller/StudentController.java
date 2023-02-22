package com.livk.redisearch.mvc.controller;

import com.livk.commons.jackson.JacksonUtils;
import com.livk.redisearch.mvc.entity.Student;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.SearchResults;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * StudentController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentController {

    private final StatefulRedisModulesConnection<String, String> connection;

    @GetMapping
    public HttpEntity<List<Student>> list(@RequestParam(defaultValue = "*") String query) {
        RedisModulesCommands<String, String> search = connection.sync();
        SearchResults<String, String> result = search.ftSearch(Student.INDEX, query);
        List<Student> studentList = result.stream()
                .map(document -> JacksonUtils.convertValueBean(document, Student.class))
                .toList();
        return ResponseEntity.ok(studentList);
    }
}
