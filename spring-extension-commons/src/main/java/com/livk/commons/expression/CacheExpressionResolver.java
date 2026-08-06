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

import lombok.Setter;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionException;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for {@link ExpressionResolver} implementations that caches compiled
 * expressions to avoid repeated parsing overhead.
 * <p>
 * Subclasses only need to implement three template methods:
 * <ul>
 * <li>{@link #compile(String)} — parse the expression string into framework-specific
 * form</li>
 * <li>{@link #transform(Context)} — convert the generic {@link Context} into framework
 * context</li>
 * <li>{@link #calculate(Object, Object, Class)} — evaluate the compiled expression</li>
 * </ul>
 * <p>
 * Optionally, subclasses may override {@link #wrapIfNecessary(String)} to pre-process the
 * raw expression string before compilation.
 *
 * @param <CONTEXT> the framework-specific context type (e.g., {@code EvaluationContext}
 * for SpEL)
 * @param <EXPRESSION> the compiled expression type (e.g., {@code Expression} for SpEL)
 * @author livk
 * @see ExpressionResolver
 * @see Context
 */
public abstract class CacheExpressionResolver<CONTEXT, EXPRESSION> implements ExpressionResolver {

	private final Map<String, EXPRESSION> expressionCache = new ConcurrentHashMap<>(256);

	@Setter
	private Environment environment;

	@Override
	public ExpressionSpec resolve(String expression) {
		return new DefaultExpressionSpec(expression);
	}

	/**
	 * Core evaluation logic: wraps, resolves placeholders, compiles (with caching),
	 * transforms context, and calculates the result.
	 * @param <T> the expected return type
	 * @param expression the expression string
	 * @param context the evaluation context
	 * @param returnType the class of the expected return type
	 * @return the evaluation result, or {@code null} if the expression is empty
	 * @throws ExpressionException if evaluation fails
	 */
	protected <T> T doEvaluate(String expression, Context context, Class<T> returnType) {
		if (ObjectUtils.isEmpty(expression)) {
			return null;
		}
		try {
			expression = wrapIfNecessary(expression);
			if (this.environment != null) {
				expression = this.environment.resolvePlaceholders(expression);
			}
			EXPRESSION compiledExpression = this.expressionCache.computeIfAbsent(expression, this::compile);
			CONTEXT frameworkContext = transform(context);
			Assert.notNull(frameworkContext, "FrameworkContext must not be null");
			return calculate(compiledExpression, frameworkContext, returnType);
		}
		catch (Throwable ex) {
			throw new ExpressionException("Expression parsing failed", ex);
		}
	}

	/**
	 * Pre-process the raw expression before compilation. Override to add
	 * framework-specific wrapping (e.g., SpEL template syntax {@code #{...}}).
	 * @param expression the raw expression string
	 * @return the potentially wrapped expression string
	 */
	protected String wrapIfNecessary(String expression) {
		return expression;
	}

	/**
	 * Compile the expression string into the framework-specific compiled form.
	 * @param expression the (possibly wrapped) expression string
	 * @return the compiled expression object
	 */
	protected abstract EXPRESSION compile(String expression);

	/**
	 * Transform the generic {@link Context} into the framework-specific context type.
	 * @param context the generic expression context
	 * @return the framework-specific context
	 */
	protected abstract CONTEXT transform(Context context);

	/**
	 * Evaluate the compiled expression against the given context.
	 * @param <T> the expected return type
	 * @param expression the compiled expression
	 * @param context the framework-specific context
	 * @param returnType the class of the expected return type
	 * @return the evaluation result
	 */
	protected abstract <T> T calculate(EXPRESSION expression, CONTEXT context, Class<T> returnType);

	private final class DefaultExpressionSpec implements ExpressionSpec {

		private final String expression;

		private ContextFactory contextFactory = ContextFactory.DEFAULT_FACTORY;

		private Context context;

		private DefaultExpressionSpec(String expression) {
			this.expression = expression;
		}

		@Override
		public ExpressionSpec contextFactory(ContextFactory contextFactory) {
			Assert.notNull(contextFactory, "ContextFactory must not be null");
			this.contextFactory = contextFactory;
			return this;
		}

		@Override
		public EvaluateSpec context(Context context) {
			Assert.notNull(context, "Context must not be null");
			this.context = context;
			return this;
		}

		@Override
		public EvaluateSpec context(Map<String, ?> context) {
			Assert.notNull(context, "Context must not be null");
			return context(Context.create(context));
		}

		@Override
		public EvaluateSpec method(Method method, Object... args) {
			Assert.notNull(method, "Method must not be null");
			return context(this.contextFactory.create(method, args));
		}

		@Override
		public String evaluate() {
			return evaluate(String.class);
		}

		@Override
		public <T> T evaluate(Class<T> returnType) {
			Context evaluationContext = this.context != null ? this.context : Context.create();
			return doEvaluate(this.expression, evaluationContext, returnType);
		}

	}

}
