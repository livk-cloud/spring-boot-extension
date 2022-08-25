package com.livk.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
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
        if (ArrayUtils.isEmpty(packages)) {
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
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return classSet;
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
    private <T> T parse(Method method, Object[] args, String condition, Class<T> returnClass, boolean template, Map<String, Object> expandMap) {
        ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        Assert.notNull(parameterNames, "参数列表不能为null");
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        if (!CollectionUtils.isEmpty(expandMap)) {
            context.setVariables(expandMap);
        }
        if (template) {
            return PARSER.parseExpression(condition, ParserContext.TEMPLATE_EXPRESSION).getValue(context, returnClass);
        } else {
            return PARSER.parseExpression(condition).getValue(context, returnClass);
        }
    }

    public <T> T parseSpEL(Method method, Object[] args, String condition, Class<T> returnClass) {
        return parse(method, args, condition, returnClass, false, null);
    }

    public String parseSpEL(Method method, Object[] args, String condition) {
        return parseSpEL(method, args, condition, String.class);
    }

    public <T> T parseSpEL(Map<String, ?> variables, String condition, Class<T> returnClass) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        variables.forEach(context::setVariable);
        return PARSER.parseExpression(condition).getValue(context, returnClass);
    }

    public String parseSpEL(Map<String, ?> variables, String condition) {
        return parseSpEL(variables, condition, String.class);
    }

    public <T> T parseTemplate(Method method, Object[] args, String condition, Class<T> returnClass) {
        return parse(method, args, condition, returnClass, true, null);
    }

    public <T> T parseTemplate(Method method, Object[] args, String condition, Class<T> returnClass, Map<String, Object> expandMap) {
        return parse(method, args, condition, returnClass, true, expandMap);
    }

    public String parseTemplate(Method method, Object[] args, String condition) {
        return parseTemplate(method, args, condition, String.class);
    }

    public String parseTemplate(Method method, Object[] args, String condition, Map<String, Object> expandMap) {
        return parse(method, args, condition, String.class, true, expandMap);
    }

    public <T> T parseTemplate(Map<String, ?> variables, String condition, Class<T> returnClass) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        variables.forEach(context::setVariable);
        return PARSER.parseExpression(condition, ParserContext.TEMPLATE_EXPRESSION).getValue(context, returnClass);
    }

    public String parseTemplate(Map<String, ?> variables, String condition) {
        return parseTemplate(variables, condition, String.class);
    }

}
