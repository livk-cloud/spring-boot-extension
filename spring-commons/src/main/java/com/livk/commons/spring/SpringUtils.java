package com.livk.commons.spring;

import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
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

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

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
     * {@example env= "spring.data.redisson.host=127.0.0.1" keyPrefix="spring.data" result=Map.of("redisson.host","127.0.0.1")}
     *
     * @param environment env
     * @param keyPrefix   prefix
     * @return map
     */
    public Map<String, String> getSubProperties(Environment environment, String keyPrefix) {
        return Binder.get(environment).bind(keyPrefix, Bindable.mapOf(String.class, String.class))
                .orElseGet(Collections::emptyMap);
    }

    /**
     * 解析SpEL表达式
     * 非模板表达式: #username
     * 模板表达式:"livk:#{#username}"
     *
     * @param method      方法
     * @param args        方法参数
     * @param condition   表达式
     * @param returnClass 返回类型
     * @param template    是否启用模板
     * @param expandMap   拓展数据
     * @param <T>         类型
     * @return T
     */
    @SuppressWarnings("unchecked")
    private <T> T parse(Method method, Object[] args, String condition, Class<T> returnClass, boolean template, Map<String, ?> expandMap) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (method != null && !ObjectUtils.isEmpty(args)) {
            ParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);
            Assert.notNull(parameterNames, "参数列表不能为null");
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        if (!CollectionUtils.isEmpty(expandMap)) {
            expandMap.forEach(context::setVariable);
        }
        if (template) {
            return PARSER.parseExpression(condition, ParserContext.TEMPLATE_EXPRESSION).getValue(context, returnClass);
        } else {
            if (returnClass.isInstance(condition) && !condition.contains("#")) {
                return (T) condition;
            }
            return PARSER.parseExpression(condition).getValue(context, returnClass);
        }
    }

    /**
     * 解析SpEL表达式
     *
     * @param method      方法
     * @param args        方法参数
     * @param condition   表达式
     * @param returnClass 返回类型
     * @param <T>         类型
     * @return T
     */
    public <T> T parseSpEL(Method method, Object[] args, String condition, Class<T> returnClass) {
        return parse(method, args, condition, returnClass, false, Map.of());
    }

    /**
     * 解析SpEL表达式
     * 非模板表达式: #username
     * 模板表达式:"livk:#{#username}"
     *
     * @param method    方法
     * @param args      方法参数
     * @param condition 表达式
     * @return string
     */
    public String parseSpEL(Method method, Object[] args, String condition) {
        return parseSpEL(method, args, condition, String.class);
    }

    /**
     * 解析SpEL表达式
     *
     * @param variables   需要填充的数据
     * @param condition   表达式
     * @param returnClass 返回类型
     * @param <T>         类型
     * @return T
     */
    public <T> T parseSpEL(Map<String, ?> variables, String condition, Class<T> returnClass) {
        return parse(null, null, condition, returnClass, false, variables);
    }

    /**
     * 解析SpEL表达式
     *
     * @param variables 需要填充的数据
     * @param condition 表达式
     * @return string
     */
    public String parseSpEL(Map<String, ?> variables, String condition) {
        return parseSpEL(variables, condition, String.class);
    }

    /**
     * 解析SpEL模板表达式
     *
     * @param method    方法
     * @param args      方法参数
     * @param condition 表达式
     * @return string
     */
    public String parseTemplate(Method method, Object[] args, String condition) {
        return parseTemplate(method, args, condition, null);
    }

    /**
     * 解析SpEL模板表达式
     *
     * @param method    方法
     * @param args      方法参数
     * @param condition 表达式
     * @param expandMap 拓展数据
     * @return string
     */
    public String parseTemplate(Method method, Object[] args, String condition, Map<String, ?> expandMap) {
        return parse(method, args, condition, String.class, true, expandMap);
    }

    /**
     * 解析SpEL模板表达式
     *
     * @param variables 需要填充的数据
     * @param condition 表达式
     * @return string
     */
    public String parseTemplate(Map<String, ?> variables, String condition) {
        return parse(null, null, condition, String.class, true, variables);
    }

    /**
     * 通用解析 spEL与Template
     *
     * @param method    方法
     * @param args      方法参数
     * @param condition 表达式
     * @return string
     */
    public String parse(Method method, Object[] args, String condition) {
        return parse(method, args, condition, null);
    }

    /**
     * 通用解析 spEL与Template
     *
     * @param method    方法
     * @param args      方法参数
     * @param condition 表达式
     * @param expandMap 拓展数据
     * @return string
     */
    public String parse(Method method, Object[] args, String condition, Map<String, ?> expandMap) {
        String result = parseTemplate(method, args, condition, expandMap);
        if (condition.equals(result)) {
            result = parse(method, args, condition, String.class, false, expandMap);
        }
        return result;
    }

    /**
     * 通用解析 spEL与Template
     *
     * @param variables 需要填充的数据
     * @param condition 表达式
     * @return string
     */
    public String parse(Map<String, ?> variables, String condition) {
        return parse(null, null, condition, variables);
    }
}
