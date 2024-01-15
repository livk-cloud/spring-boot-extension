package com.livk.core.mybatisplugins.type.postgresql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.postgresql.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * <p>
 * MybatisConfig
 * </p>
 *
 * @author livk
 */
@Configuration
class MybatisConfig {

	@Bean(destroyMethod = "close")
	public HikariDataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:postgresql://livk.com:5432/mybatis_type_context");
		dataSource.setUsername("postgres");
		dataSource.setPassword("123456");
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
		configurer.setBasePackage("com.livk.core.mybatisplugins.type.postgresql");
		configurer.setAnnotationClass(Mapper.class);
		return configurer;
	}

}
