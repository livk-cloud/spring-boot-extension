package com.livk.commons.spring.el;

import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author livk
 */
public interface EvaluationContextFactory {

    EvaluationContextFactory INSTANCE = new DefaultEvaluationContextFactory();

    default EvaluationContext eval(Method method, Object[] args) {
        return eval(method, args, Map.of());
    }

    default EvaluationContext eval(Map<String, ?> expandMap) {
        return eval(null, null, expandMap);
    }

    EvaluationContext eval(Method method, Object[] args, Map<String, ?> expandMap);
}
