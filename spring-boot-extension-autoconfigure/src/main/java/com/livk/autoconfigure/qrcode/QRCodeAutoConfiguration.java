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

package com.livk.autoconfigure.qrcode;

import com.google.zxing.common.BitMatrix;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.qrcode.QRCodeGenerator;
import com.livk.context.qrcode.resolver.QRCodeMethodReturnValueHandler;
import com.livk.context.qrcode.resolver.ReactiveQRCodeMethodReturnValueHandler;
import com.livk.context.qrcode.support.GoogleQRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * QRCodeAutoConfiguration
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
@ConditionalOnClass(BitMatrix.class)
public class QRCodeAutoConfiguration {

	/**
	 * Google qr code generator qr code generator.
	 * @param builder the builder
	 * @return the qr code generator
	 */
	@Bean
	public QRCodeGenerator googleQRCodeGenerator(Jackson2ObjectMapperBuilder builder) {
		return GoogleQRCodeGenerator.of(builder.build());
	}

	/**
	 * The type Web mvc qr code auto configuration.
	 */
	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public static class WebMvcQRCodeAutoConfiguration implements WebMvcConfigurer {

		private final QRCodeGenerator qrCodeGenerator;

		@Override
		public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
			handlers.add(new QRCodeMethodReturnValueHandler(qrCodeGenerator));
		}

	}

	/**
	 * The type Web flux qr code auto configuration.
	 */
	@AutoConfiguration
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public static class WebFluxQRCodeAutoConfiguration implements WebFluxConfigurer {

		/**
		 * Reactive qr code method return value handler reactive qr code method return
		 * value handler.
		 * @param qrCodeGenerator the qr code generator
		 * @return the reactive qr code method return value handler
		 */
		@Bean
		public ReactiveQRCodeMethodReturnValueHandler reactiveQRCodeMethodReturnValueHandler(
				QRCodeGenerator qrCodeGenerator) {
			return new ReactiveQRCodeMethodReturnValueHandler(qrCodeGenerator);
		}

	}

}
