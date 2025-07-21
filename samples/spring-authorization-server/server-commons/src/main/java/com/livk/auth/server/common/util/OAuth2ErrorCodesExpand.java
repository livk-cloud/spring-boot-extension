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

package com.livk.auth.server.common.util;

import lombok.experimental.UtilityClass;

/**
 * @author livk
 */
@UtilityClass
public class OAuth2ErrorCodesExpand {

	/**
	 * 用户名未找到
	 */
	public static final String USERNAME_NOT_FOUND = "username_not_found";

	/**
	 * 错误凭证
	 */
	public static final String BAD_CAPTCHA = "bad_captcha";

	/**
	 * 错误凭证
	 */
	public static final String BAD_CREDENTIALS = "bad_credentials";

	/**
	 * 用户被锁
	 */
	public static final String USER_LOCKED = "user_locked";

	/**
	 * 用户禁用
	 */
	public static final String USER_DISABLE = "user_disable";

	/**
	 * 用户过期
	 */
	public static final String USER_EXPIRED = "user_expired";

	/**
	 * 证书过期
	 */
	public static final String CREDENTIALS_EXPIRED = "credentials_expired";

	/**
	 * scope 为空异常
	 */
	public static final String SCOPE_IS_EMPTY = "scope_is_empty";

	/**
	 * 令牌不存在
	 */
	public static final String TOKEN_MISSING = "token_missing";

	/**
	 * 未知的登录异常
	 */
	public static final String UN_KNOW_LOGIN_ERROR = "un_know_login_error";

}
