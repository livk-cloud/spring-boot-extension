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

package com.livk.context.mybatis.type.postgresql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.testcontainers.containers.PostgresqlContainer;
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
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig(PostgresJsonTypeHandlerTests.MybatisConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class PostgresJsonTypeHandlerTests {

	@Container
	@ServiceConnection
	static final PostgresqlContainer postgresql = new PostgresqlContainer().withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("mybatis");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", postgresql::getUsername);
		registry.add("spring.datasource.password", postgresql::getPassword);
		registry.add("spring.datasource.url", () -> "jdbc:postgresql://" + postgresql.getHost() + ":"
				+ postgresql.getFirstMappedPort() + "/" + postgresql.getDatabaseName());
	}

	@Autowired
	DataSource dataSource;

	@Autowired
	UserMapper userMapper;

	@BeforeEach
	void init() throws SQLException {
		ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("type-pgsql-table.sql"));
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

		assertThat(userMapper.insert(user)).isEqualTo(1);
		User first = userMapper.selectList().getFirst();
		assertThat(first.getUsername()).isEqualTo(user.getUsername());
		assertThat(first.getPassword()).isEqualTo(user.getPassword());
		assertThat(first.getDes()).isEqualTo(user.getDes());
		assertThat(first.getDes().get("mark").asText()).isEqualTo("livk");
	}

	@TestConfiguration
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
			PostgresJsonTypeHandler typeHandler = new PostgresJsonTypeHandler(new ObjectMapper());
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
	public interface UserMapper {

		@Insert("insert into \"user\" (username,password,des) values (#{username},#{password},#{des})")
		int insert(User user);

		@Select("select * from \"user\"")
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
