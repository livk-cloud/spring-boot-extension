package com.livk.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
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

}
