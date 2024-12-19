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

package com.livk.context.mybatis.inject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.livk.commons.util.DateUtils;
import com.livk.context.mybatis.inject.annotation.SqlFunction;
import com.livk.context.mybatis.inject.enums.FunctionType;
import com.livk.context.mybatis.inject.enums.SqlFill;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.h2.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * SqlDataInjectionTest
 * </p>
 *
 * @author livk
 */
@SpringJUnitConfig(SqlDataInjectionTest.MybatisConfig.class)
class SqlDataInjectionTest {

	@Autowired
	UserMapper userMapper;

	@Autowired
	DataSource dataSource;

	@BeforeEach
	void init() throws SQLException {
		ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("inject-table.sql"));
	}

	@Test
	void test() {
		User user = new User();
		user.setUsername("livk");

		userMapper.insert(user);

		User copy = userMapper.getById(1);
		assertEquals(user.getUsername(), copy.getUsername());
		assertNotNull(copy.getInsertTime());
		assertNotNull(copy.getUpdateTime());
	}

	@TestConfiguration
	static class MybatisConfig {

		@Bean(destroyMethod = "close")
		public HikariDataSource dataSource() {
			HikariDataSource dataSource = new HikariDataSource();
			dataSource.setDriverClassName(Driver.class.getName());
			dataSource.setJdbcUrl("jdbc:h2:mem:test");
			return dataSource;
		}

		@Bean
		public SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
			Environment environment = new Environment("mybatis", new SpringManagedTransactionFactory(), dataSource);
			org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(
					environment);
			configuration.setMapUnderscoreToCamelCase(true);
			configuration.addInterceptor(new SqlDataInjection());
			configuration.setLogImpl(StdOutImpl.class);
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

		@Select("select * from `user` where id = #{id}")
		User getById(@Param("id") Integer id);

		@Insert("insert into `user`(username, insert_time, update_time) "
				+ "values(#{user.username}, #{user.insertTime}, #{user.updateTime})")
		void insert(@Param("user") User user);

	}

	@Data
	static class User implements Serializable {

		@Serial
		private static final long serialVersionUID = 1L;

		private Integer id;

		private String username;

		@JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
		@SqlFunction(fill = SqlFill.INSERT, time = FunctionType.DATE)
		private Date insertTime;

		@JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
		@SqlFunction(fill = SqlFill.INSERT_UPDATE, time = FunctionType.DATE)
		private Date updateTime;

	}

}
