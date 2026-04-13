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

package com.livk.autoconfigure.dynamic.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.diagnostics.FailureAnalysis;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class PrimaryAbstractFailureAnalyzerTests {

	final PrimaryAbstractFailureAnalyzer analyzer = new PrimaryAbstractFailureAnalyzer();

	@Test
	void analyzeReturnsFailureAnalysis() {
		PrimaryNotFountException cause = new PrimaryNotFountException("missing primary datasource");
		FailureAnalysis analysis = analyzer.analyze(cause, cause);
		assertThat(analysis).isNotNull();
		assertThat(analysis.getDescription()).isEqualTo("missing primary datasource");
		assertThat(analysis.getAction()).contains("spring.dynamic.primary");
	}

}
