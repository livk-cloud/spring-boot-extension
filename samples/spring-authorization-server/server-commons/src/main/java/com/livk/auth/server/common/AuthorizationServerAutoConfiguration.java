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

package com.livk.auth.server.common;

import com.livk.auth.server.common.core.UserDetailsAuthenticationProvider;
import com.livk.auth.server.common.service.Oauth2UserDetailsService;
import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
public class AuthorizationServerAutoConfiguration {

	@Bean
	public UserDetailsAuthenticationProvider userDetailsAuthenticationProvider(PasswordEncoder passwordEncoder,
			ObjectProvider<Oauth2UserDetailsService> oauth2UserDetailsServices) {
		return new UserDetailsAuthenticationProvider(passwordEncoder, oauth2UserDetailsServices);
	}

}
