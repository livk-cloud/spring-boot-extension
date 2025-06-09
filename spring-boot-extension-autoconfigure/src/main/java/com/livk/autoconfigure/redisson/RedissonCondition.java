package com.livk.autoconfigure.redisson;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * <p>
 * RedissonCondition
 * </p>
 *
 * @author livk
 */
class RedissonCondition extends SpringBootCondition {

	private static final String PREFIX = RedissonProperties.PREFIX + ".config";

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Environment environment = context.getEnvironment();
		BindResult<Map<String, Object>> result = Binder.get(environment)
			.bind(PREFIX, Bindable.mapOf(String.class, Object.class));
		return new ConditionOutcome(result.isBound(), "Redisson Config is loaded");
	}

}
