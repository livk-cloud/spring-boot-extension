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

package com.livk.auth.server.common.core;

import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.core.exception.BadCaptchaException;
import com.livk.auth.server.common.service.Oauth2UserDetailsService;
import com.livk.auth.server.common.util.MessageSourceUtils;
import com.livk.commons.util.ObjectUtils;
import com.livk.commons.util.HttpServletUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * @author livk
 */
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	/**
	 * The plaintext password used to perform PasswordEncoder#matches(CharSequence,
	 * String)} on when the user is not found to avoid SEC-2056.
	 */
	private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

	private final static BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();

	private final ObjectProvider<Oauth2UserDetailsService> oauth2UserDetailsServices;

	@Getter
	private PasswordEncoder passwordEncoder;

	/**
	 * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
	 * on when the user is not found to avoid SEC-2056. This is necessary, because some
	 * {@link PasswordEncoder} implementations will short circuit if the password is not
	 * in a valid format.
	 */
	private volatile String userNotFoundEncodedPassword;

	@Getter
	@Setter
	private UserDetailsService userDetailsService;

	@Setter
	private UserDetailsPasswordService userDetailsPasswordService;

	public UserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder,
			ObjectProvider<Oauth2UserDetailsService> oauth2UserDetailsServices) {
		setMessageSource(MessageSourceUtils.get());
		setPasswordEncoder(passwordEncoder);
		this.oauth2UserDetailsServices = oauth2UserDetailsServices;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		String grantType = HttpServletUtils.request().getParameter(OAuth2ParameterNames.GRANT_TYPE);

		if (authentication.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		// 校验验证码
		if (Objects.equals(SecurityConstants.SMS, grantType)) {
			String code = authentication.getCredentials().toString();
			if (!Objects.equals(code, "123456")) {
				this.logger.debug("Failed to authenticate since code does not match stored value");
				throw new BadCaptchaException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCaptcha", "Bad captcha"));
			}
		}
		// 校验密码
		if (Objects.equals(SecurityConstants.PASSWORD, grantType)) {
			String presentedPassword = authentication.getCredentials().toString();
			String encodedPassword = extractEncodedPassword(userDetails.getPassword());
			if (!this.passwordEncoder.matches(presentedPassword, encodedPassword)) {
				this.logger.debug("Failed to authenticate since password does not match stored value");
				throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
			}
		}

	}

	@SneakyThrows
	@Override
	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
		prepareTimingAttackProtection();

		MultiValueMap<String, String> paramMap = HttpServletUtils.params(HttpServletUtils.request());
		String grantType = paramMap.getFirst(OAuth2ParameterNames.GRANT_TYPE);
		String clientId = paramMap.getFirst(OAuth2ParameterNames.CLIENT_ID);

		if (ObjectUtils.isEmpty(clientId)) {
			clientId = basicConvert.convert(HttpServletUtils.request()).getName();
		}

		String finalClientId = clientId;
		Optional<Oauth2UserDetailsService> optional = oauth2UserDetailsServices.stream()
			.filter(service -> service.support(finalClientId, grantType))
			.max(Comparator.comparingInt(Ordered::getOrder));

		Oauth2UserDetailsService oauth2UserDetailsService = optional
			.orElseThrow((() -> new InternalAuthenticationServiceException("UserDetailsService error , not register")));

		try {
			UserDetails loadedUser = oauth2UserDetailsService.loadUserByUsername(username);
			Optional.ofNullable(loadedUser)
				.orElseThrow(() -> new InternalAuthenticationServiceException(
						"UserDetailsService returned null, which is an interface contract violation"));
			return loadedUser;
		}
		catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		}
		catch (InternalAuthenticationServiceException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		boolean upgradeEncoding = this.userDetailsPasswordService != null
				&& this.passwordEncoder.upgradeEncoding(user.getPassword());
		if (upgradeEncoding) {
			String presentedPassword = authentication.getCredentials().toString();
			String newPassword = this.passwordEncoder.encode(presentedPassword);
			user = this.userDetailsPasswordService.updatePassword(user, newPassword);
		}
		return super.createSuccessAuthentication(principal, authentication, user);
	}

	private void prepareTimingAttackProtection() {
		if (this.userNotFoundEncodedPassword == null) {
			this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
		}
	}

	private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() != null) {
			String presentedPassword = authentication.getCredentials().toString();
			this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
		}
	}

	/**
	 * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
	 * not set, the password will be compared using
	 * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
	 * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
	 * types.
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.passwordEncoder = passwordEncoder;
		this.userNotFoundEncodedPassword = null;
	}

	private String extractEncodedPassword(String prefixEncodedPassword) {
		int start = prefixEncodedPassword.indexOf('}');
		return prefixEncodedPassword.substring(start + SecurityConstants.DEFAULT_ID_SUFFIX.length());
	}

}
