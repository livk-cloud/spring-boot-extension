package com.livk.context.http.factory;

import com.livk.context.http.HttpConfig;
import com.livk.context.http.HttpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
@SpringJUnitConfig(HttpConfig.class)
class HttpFactoryBeanTest {

	@Autowired
	BeanFactory beanFactory;

	@Test
	void test() throws Exception {
		HttpFactoryBean factoryBean = new HttpFactoryBean();
		factoryBean.setType(HttpService.class);
		factoryBean.setAdapterFactory(new RestClientAdapterFactory());
		factoryBean.setBeanFactory(beanFactory);
		factoryBean.afterPropertiesSet();
		assertNotNull(factoryBean.getObject());
	}

}
