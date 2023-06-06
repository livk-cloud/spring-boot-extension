/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.auth.server.common.constant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * @author livk
 */
public interface SecurityConstants {

	/**
	 * 角色前缀
	 */
	String ROLE = "ROLE_";

	/**
	 * 手机号登录
	 */
	String SMS = "sms";

	AuthorizationGrantType GRANT_TYPE_SMS = new AuthorizationGrantType(SMS);
	String PASSWORD = "password";

	AuthorizationGrantType GRANT_TYPE_PASSWORD = new AuthorizationGrantType(PASSWORD);


	/**
	 * {bcrypt} 加密的特征码
	 */
	String BCRYPT = "{bcrypt}";

	/**
	 * {noop} 加密的特征码
	 */
	String NOOP = "{noop}";

	/**
	 * 用户信息
	 */
	String DETAILS_USER = "user_info";

	/**
	 * 验证码有效期,默认 60秒
	 */
	long CODE_TIME = 60;

	/**
	 * 验证码长度
	 */
	String CODE_SIZE = "6";

	/**
	 * 短信登录 参数名称
	 */
	String SMS_PARAMETER_NAME = "mobile";

	/**
	 * 授权码模式confirm
	 */
	String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";

	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	String DEFAULT_ID_SUFFIX = "}";

	String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
}
