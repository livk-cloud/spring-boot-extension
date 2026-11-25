/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.sequence;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * Condition for {@link ConditionalOnSequenceType}.
 *
 * @author livk
 */
class OnSequenceTypeCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnSequenceType.class.getName());
		if (attributes == null) {
			return ConditionOutcome.noMatch("No @ConditionalOnSequenceType attributes");
		}
		SequenceProperties.Type expected = (SequenceProperties.Type) attributes.get("value");
		SequenceProperties.Type actual = Binder.get(context.getEnvironment())
			.bind(SequenceProperties.PREFIX + ".type", SequenceProperties.Type.class)
			.orElse(SequenceProperties.Type.SNOWFLAKE);
		ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnSequenceType.class);
		if (actual == expected) {
			return ConditionOutcome.match(message.foundExactly(actual.name()));
		}
		return ConditionOutcome.noMatch(message.found(actual.name()).items(expected.name()));
	}

}
