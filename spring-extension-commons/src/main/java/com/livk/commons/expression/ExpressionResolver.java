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

package com.livk.commons.expression;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Fluent expression resolver that supports multiple expression engines.
 * <p>
 * Usage example: <pre>{@code
 * ExpressionResolver resolver = new SpringExpressionResolver();
 * String result = resolver.resolve("#username")
 *     .method(method, args)
 *     .evaluate();
 * }</pre>
 *
 * @author livk
 * @see CacheExpressionResolver
 * @see Context
 * @see ContextFactory
 */
public interface ExpressionResolver {

	/**
	 * Start resolving the given expression string.
	 * @param expression the expression to resolve
	 * @return an {@link ExpressionSpec} for further configuration and evaluation
	 */
	ExpressionSpec resolve(String expression);

	/**
	 * Specification interface that combines context configuration with evaluation
	 * capabilities.
	 * <p>
	 * Extends {@link EvaluateSpec} so callers can directly evaluate without setting
	 * context (defaults to an empty context).
	 */
	interface ExpressionSpec extends EvaluateSpec {

		/**
		 * Override the default {@link ContextFactory} used when resolving context from
		 * method parameters.
		 * @param contextFactory the context factory to use
		 * @return this spec for further configuration
		 */
		ExpressionSpec contextFactory(ContextFactory contextFactory);

		/**
		 * Set the evaluation context directly.
		 * @param context the context containing variables for expression evaluation
		 * @return an {@link EvaluateSpec} ready for evaluation
		 */
		EvaluateSpec context(Context context);

		/**
		 * Set the evaluation context from a map.
		 * @param context the map containing variables for expression evaluation
		 * @return an {@link EvaluateSpec} ready for evaluation
		 */
		EvaluateSpec context(Map<String, ?> context);

		/**
		 * Build the evaluation context by extracting parameter names from the given
		 * method and pairing them with the provided arguments.
		 * @param method the method whose parameter names define variable names
		 * @param args the argument values corresponding to the method parameters
		 * @return an {@link EvaluateSpec} ready for evaluation
		 */
		EvaluateSpec method(Method method, Object... args);

	}

	/**
	 * Terminal interface for evaluating a fully-configured expression.
	 */
	interface EvaluateSpec {

		/**
		 * Evaluate the expression and return the result as a {@link String}.
		 * @return the evaluation result as a string
		 */
		String evaluate();

		/**
		 * Evaluate the expression and return the result cast to the specified type.
		 * @param <T> the expected return type
		 * @param returnType the class of the expected return type
		 * @return the evaluation result
		 */
		<T> T evaluate(Class<T> returnType);

	}

}
