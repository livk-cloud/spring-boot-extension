package com.livk.core.mybatisplugins.inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * SqlDataInjectionTest
 * </p>
 *
 * @author livk
 */
@ContextConfiguration(classes = MybatisConfig.class)
@ExtendWith(SpringExtension.class)
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

}
