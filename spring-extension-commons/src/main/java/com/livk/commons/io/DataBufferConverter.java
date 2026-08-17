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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.StreamUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for DataBuffer format conversion.
 *
 * @author livk
 */
@UtilityClass
public class DataBufferConverter {

	/**
	 * Default buffer size constant.
	 */
	public static final int BUFFER_SIZE = StreamUtils.BUFFER_SIZE;

	/**
	 * Default DataBufferFactory instance.
	 */
	public static final DataBufferFactory DEFAULT_FACTORY = DefaultDataBufferFactory.sharedInstance;

	/**
	 * Converts Flux DataBuffer to Mono InputStream.
	 * @param dataBufferFlux the data buffer flux
	 * @return the mono
	 */
	public Mono<InputStream> transform(Flux<DataBuffer> dataBufferFlux) {
		return DataBufferUtils.join(dataBufferFlux).map(dataBuffer -> dataBuffer.asInputStream(true));
	}

	/**
	 * Converts byte array to Flux DataBuffer.
	 * @param array the array
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(byte[] array) {
		ByteArrayResource resource = new ByteArrayResource(array);
		return DataBufferUtils.read(resource, DEFAULT_FACTORY, BUFFER_SIZE);
	}

	/**
	 * Converts Flux DataBuffer to Mono byte array.
	 * @param bufferFlux the buffer flux
	 * @return the mono
	 */
	public Mono<byte[]> transformByte(Flux<DataBuffer> bufferFlux) {
		return DataBufferConverter.transform(bufferFlux)
			.publishOn(Schedulers.boundedElastic())
			.handle((inputStream, sink) -> {
				try (inputStream) {
					sink.next(inputStream.readAllBytes());
				}
				catch (IOException ex) {
					sink.error(Exceptions.bubble(ex));
				}
			});
	}

	/**
	 * Converts InputStream to Flux DataBuffer.
	 * @param inputStream the input stream
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(InputStream inputStream) {
		return DataBufferUtils.read(new InputStreamResource(inputStream), DEFAULT_FACTORY, BUFFER_SIZE);
	}

	/**
	 * Converts Mono InputStream to Flux DataBuffer.
	 * @param inputStreamMono the input stream mono
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(Mono<InputStream> inputStreamMono) {
		return inputStreamMono.flatMapMany(DataBufferConverter::transform);
	}

}
