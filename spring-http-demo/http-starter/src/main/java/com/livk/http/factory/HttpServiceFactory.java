package com.livk.http.factory;

import com.google.common.base.CaseFormat;
import com.livk.http.annotation.BeanName;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
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
public class HttpServiceFactory implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	private final HttpServiceProxyFactory proxyFactory;

	private BeanFactory beanFactory;

	private ResourceLoader resourceLoader;

	public HttpServiceFactory() {
		WebClient client = WebClient.builder().build();
		this.proxyFactory = HttpServiceProxyFactory.builder(new WebClientAdapter(client)).build();
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
		Set<Class<?>> typesAnnotatedClass = findByAnnotationType(HttpExchange.class, packages.toArray(String[]::new));
		for (Class<?> exchangeClass : typesAnnotatedClass) {
			BeanName name = AnnotationUtils.getAnnotation(exchangeClass, BeanName.class);
			String beanName = name != null ? name.value()
					: CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, exchangeClass.getSimpleName());
			registry.registerBeanDefinition(beanName, getBeanDefinition(exchangeClass));
		}
	}

	private <T> BeanDefinition getBeanDefinition(Class<T> exchangeClass) {
		T proxyFactoryClient = proxyFactory.createClient(exchangeClass);
		return new RootBeanDefinition(exchangeClass, () -> proxyFactoryClient);
	}

	private Set<Class<?>> findByAnnotationType(Class<? extends Annotation> annotationClass, String... packages) {
		Assert.notNull(annotationClass, "annotation not null");
		Set<Class<?>> classSet = new HashSet<>();
		if (packages == null || packages.length == 0) {
			return classSet;
		}
		ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
		try {
			for (String packageStr : packages) {
				packageStr = packageStr.replace(".", "/");
				Resource[] resources = resolver.getResources("classpath*:" + packageStr + "/**/*.class");
				for (Resource resource : resources) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					String className = metadataReader.getClassMetadata().getClassName();
					Class<?> clazz = Class.forName(className);
					if (AnnotationUtils.findAnnotation(clazz, annotationClass) != null) {
						classSet.add(clazz);
					}
				}
			}
		}
		catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return classSet;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
