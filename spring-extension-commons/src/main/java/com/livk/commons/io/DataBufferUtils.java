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

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.StreamUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * DataBufferUtils功能拓展
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class DataBufferUtils extends org.springframework.core.io.buffer.DataBufferUtils {

	/**
	 * 设置默认BUFFER_SIZE
	 */
	public static final int BUFFER_SIZE = StreamUtils.BUFFER_SIZE;

	/**
	 * 设置默认DEFAULT_FACTORY.
	 */
	public static final DataBufferFactory DEFAULT_FACTORY = DefaultDataBufferFactory.sharedInstance;

	/**
	 * 转换Flux DataBuffer成Mono InputStream
	 * @param dataBufferFlux the data buffer flux
	 * @return the mono
	 */
	public Mono<InputStream> transform(Flux<DataBuffer> dataBufferFlux) {
		return join(dataBufferFlux).map(DataBuffer::asInputStream);
	}

	/**
	 * 转换byte[]成Flux DataBuffer
	 * @param array the array
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(byte[] array) {
		ByteArrayResource resource = new ByteArrayResource(array);
		return read(resource, DEFAULT_FACTORY, BUFFER_SIZE);
	}

	/**
	 * 转换Flux DataBuffer 成Mono byte[]
	 * @param bufferFlux the buffer flux
	 * @return the mono
	 */
	public Mono<byte[]> transformByte(Flux<DataBuffer> bufferFlux) {
		return DataBufferUtils.transform(bufferFlux)
			.publishOn(Schedulers.boundedElastic())
			.handle((inputStream, sink) -> {
				try {
					sink.next(inputStream.readAllBytes());
				}
				catch (IOException e) {
					sink.error(Exceptions.bubble(e));
				}
			});
	}

	/**
	 * InputStream转换成Flux DataBuffer
	 * @param inputStream the input stream
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(InputStream inputStream) {
		return read(new InputStreamResource(inputStream), DEFAULT_FACTORY, BUFFER_SIZE);
	}

	/**
	 * Mono InputStream 转换成Flux DataBuffer
	 * @param inputStreamMono the input stream mono
	 * @return the flux
	 */
	public Flux<DataBuffer> transform(Mono<InputStream> inputStreamMono) {
		return inputStreamMono.flatMapMany(DataBufferUtils::transform);
	}

}
