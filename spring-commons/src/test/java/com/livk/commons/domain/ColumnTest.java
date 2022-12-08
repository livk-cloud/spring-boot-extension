package com.livk.commons.domain;

import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * ColumnTest
 * </p>
 *
 * @author livk
 */
class ColumnTest {

    static User user;

    static Column<User> column;

    @BeforeAll
    static void beforeAll() {
        user = new User();
        user.setId(12L);
        user.setUsername("livk");
        column = Column.create(user);
    }

    @Test
    void get() {
        Long id = column.get(User::getId);
        assertEquals(12L, id);
        String username = column.get(User::getUsername);
        assertEquals("livk", username);
        assertEquals(user, column.getEntity());
        assertEquals(Map.of("id", 12L, "username", "livk"), column.getMap());
    }
}

@Data
class User {

    private Long id;

    private String username;
}
