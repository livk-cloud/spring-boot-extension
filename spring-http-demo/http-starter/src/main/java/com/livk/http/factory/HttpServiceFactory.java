package com.livk.http.factory;

import com.google.common.base.CaseFormat;
import com.livk.http.annotation.BeanName;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * HttpFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
public class HttpServiceFactory implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

	private final HttpServiceProxyFactory proxyFactory;

	private BeanFactory beanFactory;

	public HttpServiceFactory() {
		WebClient client = WebClient.builder().build();
		this.proxyFactory = HttpServiceProxyFactory.builder(new WebClientAdapter(client)).build();
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
		Reflections reflections;
		for (String packageStr : packages) {
			reflections = new Reflections(packageStr);
			Set<Class<?>> typesAnnotatedClass = reflections.getTypesAnnotatedWith(HttpExchange.class);
			for (Class<?> exchangeClass : typesAnnotatedClass) {
				BeanName name = AnnotationUtils.getAnnotation(exchangeClass, BeanName.class);
				String beanName = name != null ? name.value()
						: CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, exchangeClass.getSimpleName());
				registry.registerBeanDefinition(beanName, getBeanDefinition(exchangeClass));
			}
		}
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	private <T> BeanDefinition getBeanDefinition(Class<T> exchangeClass) {
		T proxyFactoryClient = proxyFactory.createClient(exchangeClass);
		return new RootBeanDefinition(exchangeClass, () -> proxyFactoryClient);
	}

}
