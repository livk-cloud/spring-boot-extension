package com.livk.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * SpringUtils
 * </p>
 *
 * @author livk
 * @date 2022/5/30
 */
@UtilityClass
public class SpringUtils {

	private static final SpelExpressionParser PARSER = new SpelExpressionParser();

	private static final StandardEvaluationContext CONTEXT = new StandardEvaluationContext();

	/**
	 * 获取被注解标注的class
	 * @param annotationClass annotation
	 * @param resourceLoader resourceLoader
	 * @param packages 待扫描的包
	 * @return set class
	 */
	public Set<Class<?>> findByAnnotationType(Class<? extends Annotation> annotationClass,
			ResourceLoader resourceLoader, String... packages) {
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

	public <T> T parseSpEL(Method method, Object[] args, String condition, Class<T> returnClass) {
		var discoverer = new LocalVariableTableParameterNameDiscoverer();
		var parameterNames = discoverer.getParameterNames(method);
		Assert.notNull(parameterNames, "参数列表不能为null");
		for (int i = 0; i < parameterNames.length; i++) {
			CONTEXT.setVariable(parameterNames[i], args[i]);
		}
		return PARSER.parseExpression(condition).getValue(CONTEXT, returnClass);
	}

	public String parseSpEL(Method method, Object[] args, String condition) {
		return parseSpEL(method, args, condition, String.class);
	}

	public <T> T parseSpEL(Map<String, ?> variables, String condition, Class<T> returnClass) {
		variables.forEach(CONTEXT::setVariable);
		return PARSER.parseExpression(condition).getValue(CONTEXT, returnClass);
	}

	public String parseSpEL(Map<String, ?> variables, String condition) {
		return parseSpEL(variables, condition, String.class);
	}

}
