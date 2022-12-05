package com.livk.springboot.support;

import com.livk.commons.support.SpringContextHolder;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * SpringContextHolderTest
 * </p>
 *
 * @author livk
 * @date 2022/12/5
 */
@ImportAutoConfiguration(SpringContextHolder.class)
@SpringBootTest("spring.data.redis.host=livk.com")
class SpringContextHolderTest {

    BeanTest bean = new BeanTest();


    @Test
    void getBean() {
        SpringContextHolder.registerBean(bean, "test");
        assertEquals(bean, SpringContextHolder.getBean("test"));
        assertEquals(bean, SpringContextHolder.getBean(BeanTest.class));
        assertEquals(bean, SpringContextHolder.getBean("test", BeanTest.class));
        if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
            context.removeBeanDefinition("test");
        }
    }


    @Test
    void getBeanProvider() {
        SpringContextHolder.registerBean(bean, "test");
        ResolvableType resolvableType = ResolvableType.forClass(BeanTest.class);
        assertEquals(bean, SpringContextHolder.getBeanProvider(BeanTest.class).getIfAvailable());
        assertEquals(bean, SpringContextHolder.getBeanProvider(resolvableType).getIfAvailable());
        if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
            context.removeBeanDefinition("test");
        }
    }

    @Test
    void getBeansOfType() {
        SpringContextHolder.registerBean(bean, "test");
        assertEquals(Map.of("test", bean), SpringContextHolder.getBeansOfType(BeanTest.class));
        if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
            context.removeBeanDefinition("test");
        }
    }

    @Test
    void getProperty() {
        assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host"));
        assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host", String.class));
        assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host", String.class, "livk.cn"));
        assertEquals("livk.cn", SpringContextHolder.getProperty("spring.data.redisson.host", String.class, "livk.cn"));
    }

    @Test
    void resolvePlaceholders() {
        assertEquals("livk.com", SpringContextHolder.resolvePlaceholders("${spring.data.redis.host}"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void registerBean() {
        SpringContextHolder.registerBean(bean, "test1");
        RootBeanDefinition beanDefinition = new RootBeanDefinition((Class<BeanTest>) bean.getClass(), () -> bean);
        SpringContextHolder.registerBean(beanDefinition, "test2");
        assertEquals(Map.of("test1", bean, "test2", bean), SpringContextHolder.getBeansOfType(BeanTest.class));
        if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
            context.removeBeanDefinition("test1");
            context.removeBeanDefinition("test2");
        }
    }
}

@Data
class BeanTest {
    private final Long id = 1L;
}
