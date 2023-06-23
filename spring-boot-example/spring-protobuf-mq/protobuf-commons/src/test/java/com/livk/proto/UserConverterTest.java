package com.livk.proto;

import com.livk.commons.util.ObjectUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author livk
 */
class UserConverterTest {

    static final UserConverter converter = UserConverter.INSTANCE;

    @Test
    void convert() {
        User user = new User(1L, "root", "123456@gmail.com", 0);

        byte[] bytes = converter.convert(user);
        assertNotNull(bytes);
        assertFalse(ObjectUtils.isEmpty(bytes));
        assertEquals(user, converter.convert(bytes));
    }
}
