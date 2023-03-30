package com.livk.commons.spring.util;

import com.livk.commons.spring.spel.SpringExpressionResolver;
import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * SpringUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class SpringUtils {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private static final SpringExpressionResolver RESOLVER = new SpringExpressionResolver();

    /**
     * 获取被注解标注的class
     *
     * @param annotationClass annotation
     * @param resourceLoader  resourceLoader
     * @param packages        待扫描的包
     * @return set class
     */
    public Set<Class<?>> findByAnnotationType(Class<? extends Annotation> annotationClass,
                                              ResourceLoader resourceLoader, String... packages) {
        Assert.notNull(annotationClass, "annotation not null");
        Set<Class<?>> classSet = new HashSet<>();
        if (ObjectUtils.isEmpty(packages)) {
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
                    if (AnnotationUtils.isAnnotationDeclaredLocally(annotationClass, clazz) ||
                        AnnotatedElementUtils.hasAnnotation(clazz, annotationClass)) {
                        classSet.add(clazz);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * {example env= "spring.data.redisson.host=127.0.0.1" keyPrefix="spring.data" result=Map.of("redisson.host","127.0.0.1")}
     *
     * @param environment env
     * @param keyPrefix   prefix
     * @return map sub properties
     */
    public Map<String, String> getSubPropertiesMap(Environment environment, String keyPrefix) {
        return bind(environment, keyPrefix, Bindable.mapOf(String.class, String.class))
                .orElseGet(Collections::emptyMap);
    }

    /**
     * Gets sub properties.
     *
     * @param environment the environment
     * @param keyPrefix   the key prefix
     * @return the sub properties
     */
    public Properties getSubProperties(Environment environment, String keyPrefix) {
        return bind(environment, keyPrefix, Bindable.of(Properties.class))
                .orElseGet(Properties::new);
    }

    /**
     * Bind bind result.
     *
     * @param <T>         the type parameter
     * @param environment the environment
     * @param keyPrefix   the key prefix
     * @param bindable    the bindable
     * @return the bind result
     */
    public <T> BindResult<T> bind(Environment environment, String keyPrefix, Bindable<T> bindable) {
        return Binder.get(environment).bind(keyPrefix, bindable);
    }

    /**
     * 解析SpEL表达式
     * 非模板表达式: #username
     * 模板表达式:"livk:#{#username}"
     *
     * @param <T>         类型
     * @param method      方法
     * @param args        方法参数
     * @param condition   表达式
     * @param returnClass 返回类型
     * @param expandMap   拓展数据
     * @return T
     */
    public <T> T parseSpEL(Method method, Object[] args, String condition, Class<T> returnClass, Map<String, ?> expandMap) {
        return RESOLVER.evaluate(condition, method, args, expandMap, returnClass);
    }

    /**
     * 解析SpEL表达式
     *
     * @param <T>         类型
     * @param method      方法
     * @param args        方法参数
     * @param condition   表达式
     * @param returnClass 返回类型
     * @return T t
     */
    public <T> T parseSpEL(Method method, Object[] args, String condition, Class<T> returnClass) {
        return parseSpEL(method, args, condition, returnClass, Map.of());
    }

    /**
     * 解析SpEL表达式
     *
     * @param <T>         类型
     * @param variables   需要填充的数据
     * @param condition   表达式
     * @param returnClass 返回类型
     * @return T
     */
    public <T> T parseSpEL(Map<String, ?> variables, String condition, Class<T> returnClass) {
        return parseSpEL(null, null, condition, returnClass, variables);
    }

    /**
     * 解析SpEL表达式
     * 非模板表达式: #username
     * 模板表达式:"livk:#{#username}"
     *
     * @param method    方法
     * @param args      方法参数
     * @param condition 表达式
     * @return string string
     */
    public String parseSpEL(Method method, Object[] args, String condition) {
        return parseSpEL(method, args, condition, String.class);
    }

    /**
     * 解析SpEL表达式
     *
     * @param variables 需要填充的数据
     * @param condition 表达式
     * @return string string
     */
    public String parseSpEL(Map<String, ?> variables, String condition) {
        return parseSpEL(variables, condition, String.class);
    }

    /**
     * Parse sp el string.
     *
     * @param method    the method
     * @param args      the args
     * @param condition the condition
     * @param expandMap the expand map
     * @return the string
     */
    public String parseSpEL(Method method, Object[] args, String condition, Map<String, ?> expandMap) {
        return parseSpEL(method, args, condition, String.class, expandMap);
    }
}
