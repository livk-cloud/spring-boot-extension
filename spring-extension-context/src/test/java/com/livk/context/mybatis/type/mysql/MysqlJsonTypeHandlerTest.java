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

package com.livk.context.mybatis.type.mysql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.testcontainers.containers.MysqlContainer;
import com.mysql.cj.jdbc.Driver;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * MysqlJsonTypeHandlerTest
 * </p>
 *
 * @author livk
 */
@SpringJUnitConfig(MysqlJsonTypeHandlerTest.MybatisConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class MysqlJsonTypeHandlerTest {

	@Container
	@ServiceConnection
	static MysqlContainer mysql = new MysqlContainer().withEnv("MYSQL_ROOT_PASSWORD", "123456")
		.withDatabaseName("mybatis");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.datasource.url", () -> "jdbc:mysql://" + mysql.getHost() + ":" + mysql.getMappedPort(3306)
				+ "/" + mysql.getDatabaseName() + "?createDatabaseIfNotExist=true");
	}

	@Autowired
	DataSource dataSource;

	@Autowired
	UserMapper userMapper;

	@BeforeEach
	void init() throws SQLException {
		ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("type-mysql-table.sql"));
	}

	@Test
	void test() {
		String json = """
				{
				  "mark": "livk"
				}""";
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setDes(JsonMapperUtils.readTree(json));

		userMapper.insert(user);
		User first = userMapper.selectList().getFirst();
		assertEquals(user.getUsername(), first.getUsername());
		assertEquals(user.getPassword(), first.getPassword());
		assertEquals(user.getDes(), first.getDes());
		assertEquals("livk", first.getDes().get("mark").asText());
	}

	@Configuration
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	static class MybatisConfig {

		@Bean(destroyMethod = "close")
		public HikariDataSource dataSource(@Value("${spring.datasource.url}") String url,
				@Value("${spring.datasource.username}") String username,
				@Value("${spring.datasource.password}") String password) {
			HikariDataSource dataSource = new HikariDataSource();
			dataSource.setDriverClassName(Driver.class.getName());
			dataSource.setJdbcUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
		}

		@Bean
		public SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
			Environment environment = new Environment("mybatis", new SpringManagedTransactionFactory(), dataSource);
			org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(
					environment);
			configuration.setMapUnderscoreToCamelCase(true);
			configuration.setLogImpl(StdOutImpl.class);
			MysqlJsonTypeHandler typeHandler = new MysqlJsonTypeHandler(new ObjectMapper());
			configuration.getTypeHandlerRegistry().register(typeHandler);
			return new DefaultSqlSessionFactory(configuration);
		}

		@Bean
		public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
			return new SqlSessionTemplate(sqlSessionFactory);
		}

		@Bean
		public MapperScannerConfigurer mapperScannerConfigurer() {
			MapperScannerConfigurer configurer = new MapperScannerConfigurer();
			configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
			configurer.setSqlSessionTemplateBeanName("sqlSessionTemplate");
			configurer.setBasePackage(this.getClass().getPackageName());
			configurer.setAnnotationClass(Mapper.class);
			return configurer;
		}

	}

	@Mapper
	interface UserMapper {

		@Insert("insert into user (username,password,des) values (#{username},#{password},#{des})")
		int insert(User user);

		@Select("select * from user")
		List<User> selectList();

	}

	@Data
	static class User {

		private Long id;

		private String username;

		private String password;

		private JsonNode des;

	}

}
