/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.JsonMapperUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

/**
 * @author livk
 */
class DataBufferUtilsTest {

	@Test
	void transform() throws IOException {
		String text = "123456";
		StepVerifier.create(toMonoStr(DataBufferUtils.transform(text.getBytes()))).expectNext(text).verifyComplete();

		ClassPathResource resource = new ClassPathResource("input.json");
		StepVerifier
			.create(toMonoStr(DataBufferUtils.transform(resource.getInputStream())).map(JsonMapperUtils::readTree))
			.expectNext(JsonMapperUtils.readValue(resource.getInputStream(), JsonNode.class))
			.verifyComplete();

		StepVerifier
			.create(toMonoStr(DataBufferUtils.transform(Mono.just(resource.getInputStream())))
				.map(JsonMapperUtils::readTree))
			.expectNext(JsonMapperUtils.readValue(resource.getInputStream(), JsonNode.class))
			.verifyComplete();
	}

	private Mono<String> toMonoStr(Flux<DataBuffer> bufferFlux) {
		return DataBufferUtils.transformByte(bufferFlux).map(String::new);
	}

}
