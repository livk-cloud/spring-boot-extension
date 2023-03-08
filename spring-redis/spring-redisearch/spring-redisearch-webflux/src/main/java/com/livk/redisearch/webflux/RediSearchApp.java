package com.livk.redisearch.webflux;

import com.livk.commons.jackson.JacksonUtils;
import com.livk.commons.spring.SpringLauncher;
import com.livk.commons.util.ReflectionUtils;
import com.livk.redisearch.webflux.entity.Student;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.Document;
import com.redis.lettucemod.search.Field;
import com.redis.lettucemod.search.SearchResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Random;

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
        SpringLauncher.run(RediSearchApp.class, args);
    }

    @Bean
    @SuppressWarnings("unchecked")
    public ApplicationRunner applicationRunner(RedisModulesClient redisModulesClient) {
        return (args) -> {
            StatefulRedisModulesConnection<String, String> connect = redisModulesClient.connect();
            RedisModulesCommands<String, String> search = connect.sync();

            if (!search.ftList().contains(Student.INDEX)) {
                search.ftCreate(Student.INDEX,
                        Field.text(ReflectionUtils.getFieldName(Student::getName)).weight(5.0).build(),
                        Field.text(ReflectionUtils.getFieldName(Student::getSex)).build(),
                        Field.text(ReflectionUtils.getFieldName(Student::getDesc)).build(),
                        Field.tag("class").build());
            }
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int randomNum = random.nextInt(2);
                Student student = new Student()
                        .setName("livk-" + i)
                        .setSex(randomNum == 0 ? "男" : "女")
                        .setDesc("是一个学生")
                        .setClassX((i + 1) + "班");
                Map<String, String> body = JacksonUtils.convertValueMap(student, String.class, String.class);
                search.hmset("00" + i, body);
            }
            SearchResults<String, String> result = search.ftSearch(Student.INDEX, "*");
            for (Document<String, String> document : result) {
                Student bean = JacksonUtils.convertValue(document, Student.class);
                log.info("{}", bean);
            }
        };
    }
}
