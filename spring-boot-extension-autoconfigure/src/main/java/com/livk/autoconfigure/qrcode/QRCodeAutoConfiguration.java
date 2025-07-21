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

package com.livk.autoconfigure.qrcode;

import com.google.zxing.common.BitMatrix;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.qrcode.QrCodeManager;
import com.livk.context.qrcode.resolver.QrCodeMethodArgumentResolver;
import com.livk.context.qrcode.resolver.QrCodeMethodReturnValueHandler;
import com.livk.context.qrcode.resolver.ReactiveQrCodeMethodArgumentResolver;
import com.livk.context.qrcode.resolver.ReactiveQrCodeMethodReturnValueHandler;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
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
	public QrCodeManager googleQRCodeManager(Jackson2ObjectMapperBuilder builder) {
		return GoogleQrCodeManager.of(builder.build());
	}

	/**
	 * The type Web mvc qr code auto configuration.
	 */
	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public static class WebMvcQRCodeAutoConfiguration implements WebMvcConfigurer {

		private final QrCodeManager qrCodeManager;

		@Override
		public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
			handlers.add(new QrCodeMethodReturnValueHandler(qrCodeManager));
		}

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
			resolvers.add(new QrCodeMethodArgumentResolver(qrCodeManager));
		}

	}

	/**
	 * The type Web flux qr code auto configuration.
	 */
	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public static class WebFluxQRCodeAutoConfiguration implements WebFluxConfigurer {

		private final QrCodeManager qrCodeManager;

		/**
		 * Reactive qr code method return value handler reactive qr code method return
		 * value handler.
		 * @return the reactive qr code method return value handler
		 */
		@Bean
		public ReactiveQrCodeMethodReturnValueHandler reactiveQRCodeMethodReturnValueHandler() {
			return new ReactiveQrCodeMethodReturnValueHandler(qrCodeManager);
		}

		@Override
		public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
			configurer.addCustomResolver(new ReactiveQrCodeMethodArgumentResolver(qrCodeManager));
		}

	}

}
