package com.livk.ck.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * UserMapperTest
 * </p>
 *
 * @author livk
 * @date 2022/4/26
 */
@SpringBootTest
public class UserMapperTest {

	@Autowired
	UserMapper userMapper;

	@Test
	void testList() {
		Assertions.assertNotNull(userMapper.list());
	}

}
