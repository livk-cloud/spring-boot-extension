package com.livk.caffeine.util;

import lombok.experimental.UtilityClass;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.TreeMap;

/**
 * <p>
 * SpelUtils
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@UtilityClass
public class SpelUtils {

    public String parse(String spel, TreeMap<String, Object> map) {
        spel = String.format("#{%s}", spel);
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        map.forEach(context::setVariable);
        Expression expression = parser.parseExpression(spel, new TemplateParserContext());
        return expression.getValue(context, String.class);
    }
}
