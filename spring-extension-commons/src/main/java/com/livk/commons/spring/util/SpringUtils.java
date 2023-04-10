package com.livk.commons.spring.util;

import com.livk.commons.spring.context.AnnotationMetadataResolver;
import com.livk.commons.spring.spel.SpringExpressionResolver;
import lombok.experimental.UtilityClass;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * SpringUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class SpringUtils {
    private static final SpringExpressionResolver RESOLVER = new SpringExpressionResolver();

    /**
     * 获取被注解标注的class
     *
     * @param annotationType annotation
     * @param resourceLoader resourceLoader
     * @param packages       待扫描的包
     * @return set class
     */
    public Set<Class<?>> findByAnnotationType(Class<? extends Annotation> annotationType,
                                              ResourceLoader resourceLoader, String... packages) {
        Assert.notNull(annotationType, "annotation not null");
        return new AnnotationMetadataResolver(resourceLoader, packages).find(annotationType);
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
     * @return T
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
     * 解析SpEL表达式
     *
     * @param method    方法
     * @param args      方法参数
     * @param condition 表达式
     * @param expandMap 需要填充的数据
     * @return the string
     */
    public String parseSpEL(Method method, Object[] args, String condition, Map<String, ?> expandMap) {
        return parseSpEL(method, args, condition, String.class, expandMap);
    }
}
