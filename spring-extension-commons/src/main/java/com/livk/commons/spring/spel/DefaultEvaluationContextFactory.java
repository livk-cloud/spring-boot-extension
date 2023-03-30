package com.livk.commons.spring.spel;

import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Default evaluation context factory.
 *
 * @author livk
 */
class DefaultEvaluationContextFactory implements EvaluationContextFactory {

    private final Map<Method, String[]> parameterNamesCache = new ConcurrentHashMap<>(64);

    private final ParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();

    @Override
    public EvaluationContext eval(Method method, Object[] args, Map<String, ?> expandMap) {
        StandardEvaluationContext context = evaluationContext();
        if (method != null) {
            String[] parameterNames = this.parameterNamesCache.computeIfAbsent(method, discoverer::getParameterNames);
            Assert.notNull(parameterNames, "参数列表不能为null");
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        if (!CollectionUtils.isEmpty(expandMap)) {
            expandMap.forEach(context::setVariable);
        }
        return context;
    }

    private StandardEvaluationContext evaluationContext() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.addPropertyAccessor(new BeanExpressionContextAccessor());
        context.addPropertyAccessor(new BeanFactoryAccessor());
        context.addPropertyAccessor(new MapAccessor());
        context.addPropertyAccessor(new EnvironmentAccessor());
        context.setTypeLocator(new StandardTypeLocator());
        context.setTypeConverter(new StandardTypeConverter());
        return context;
    }
}
