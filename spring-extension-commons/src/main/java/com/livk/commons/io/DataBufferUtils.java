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

package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

/**
 * Extended utilities for DataBuffer operations.
 *
 * @author livk
 * @deprecated use {@link com.livk.commons.io.DataBufferConverter}
 */
@UtilityClass
@Deprecated(since = "2.1.1")
public class DataBufferUtils extends org.springframework.core.io.buffer.DataBufferUtils {

	/**
	 * Converts Flux DataBuffer to Mono InputStream.
	 * @param dataBufferFlux the data buffer flux
	 * @return the mono
	 */
	public Mono<InputStream> transform(Flux<DataBuffer> dataBufferFlux) {
		return DataBufferConverter.transform(dataBufferFlux);
	}

	/**
	 * Converts byte array to Flux DataBuffer.
	 * @param array the array
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(byte[] array) {
		return DataBufferConverter.transform(array);
	}

	/**
	 * Converts Flux DataBuffer to Mono byte array.
	 * @param bufferFlux the buffer flux
	 * @return the mono
	 */
	public Mono<byte[]> transformByte(Flux<DataBuffer> bufferFlux) {
		return DataBufferConverter.transformByte(bufferFlux);
	}

	/**
	 * Converts InputStream to Flux DataBuffer.
	 * @param inputStream the input stream
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(InputStream inputStream) {
		return DataBufferConverter.transform(inputStream);
	}

	/**
	 * Converts Mono InputStream to Flux DataBuffer.
	 * @param inputStreamMono the input stream mono
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(Mono<InputStream> inputStreamMono) {
		return DataBufferConverter.transform(inputStreamMono);
	}

}
