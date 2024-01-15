package com.livk.core.mybatisplugins.type.postgresql;

import com.livk.commons.jackson.util.JsonMapperUtils;
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

/**
 * <p>
 * PostgresJsonTypeHandlerTest
 * </p>
 *
 * @author livk
 */
@ContextConfiguration(classes = MybatisConfig.class)
@ExtendWith(SpringExtension.class)
class PostgresJsonTypeHandlerTest {

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

		userMapper.insert(user);
		User first = userMapper.selectList().getFirst();
		assertEquals(user.getUsername(), first.getUsername());
		assertEquals(user.getPassword(), first.getPassword());
		assertEquals(user.getDes(), first.getDes());
		assertEquals("livk", first.getDes().get("mark").asText());
	}

}
