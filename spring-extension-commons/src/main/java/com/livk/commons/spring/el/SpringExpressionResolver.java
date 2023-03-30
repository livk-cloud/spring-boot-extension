package com.livk.commons.spring.el;

import lombok.Setter;
import org.springframework.core.env.Environment;
import org.springframework.expression.*;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author livk
 */
public class SpringExpressionResolver implements ExpressionResolver {

    private final ExpressionParser expressionParser;

    private final Map<String, Expression> expressionCache = new ConcurrentHashMap<>(256);

    private final ParserContext beanExpressionParserContext = new TemplateParserContext();

    @Setter
    private Environment environment;

    public SpringExpressionResolver() {
        this.expressionParser = new SpelExpressionParser();
    }

    public SpringExpressionResolver(ClassLoader beanClassLoader) {
        this.expressionParser = new SpelExpressionParser(new SpelParserConfiguration(null, beanClassLoader));
    }

    public SpringExpressionResolver(Environment environment) {
        this();
        this.environment = environment;
    }

    @Override
    public <T> T evaluate(String value, EvaluationContext context, Class<T> returnType) {
        if (!StringUtils.hasLength(value)) {
            return null;
        }
        try {
            value = wrapIfNecessary(value);
            if (environment != null) {
                value = environment.resolvePlaceholders(value);
            }
            Expression expression = this.expressionCache.get(value);
            if (expression == null) {
                expression = this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
                this.expressionCache.put(value, expression);
            }
            return expression.getValue(context, returnType);
        } catch (Throwable ex) {
            throw new ExpressionException("Expression parsing failed", ex);
        }
    }

    private String wrapIfNecessary(String expression) {
        if (!expression.contains("#")) {
            return expression;
        }
        if (!expression.contains("#{")) {
            return "#{" + expression + "}";
        }
        return expression;
    }
}
