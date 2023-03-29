package com.livk.commons.cglib;

import com.livk.commons.function.FieldFunc;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class BeanMapTest {
    @Test
    @SuppressWarnings("unchecked")
    public void test() {
        List<Bean> list = List.of(
                new Bean().setId(1L).setUsername("root"),
                new Bean().setId(2L).setUsername("root"),
                new Bean().setId(3L).setUsername("root")
        );
        Bean bean = new Bean().setId(0L)
                .setUsername("livk")
                .setBeans(list);

        BeanMap beanMap = BeanMap.create(bean);
        assertEquals(0L, beanMap.get(FieldFunc.getName(Bean::getId)));
        assertEquals("livk", beanMap.get(FieldFunc.getName(Bean::getUsername)));
        assertEquals(Set.of(list), Set.of(beanMap.get(FieldFunc.getName(Bean::getBeans))));

        assertEquals(1L, beanMap.get(list.get(0), FieldFunc.getName(Bean::getId)));

        assertEquals(Long.class, beanMap.getPropertyType(FieldFunc.getName(Bean::getId)));
        assertEquals(String.class, beanMap.getPropertyType(FieldFunc.getName(Bean::getUsername)));
        assertEquals(List.class, beanMap.getPropertyType(FieldFunc.getName(Bean::getBeans)));
    }

    @Data
    @Accessors(chain = true)
    static class Bean {
        private Long id;

        private String username;

        private List<Bean> beans;
    }
}
