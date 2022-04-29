package com.livk.caffeine.util;

import lombok.experimental.UtilityClass;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

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

	public String parse(String spel, Map<String, ?> map) {
		spel = String.format("#{%s}", spel);
		var parser = new SpelExpressionParser();
		var context = new StandardEvaluationContext();
		map.forEach(context::setVariable);
		var expression = parser.parseExpression(spel, new TemplateParserContext());
		return expression.getValue(context, String.class);
	}

}
