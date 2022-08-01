package com.livk.auth.server.common.constant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * <p>
 * OAuth2Constants
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
public interface OAuth2Constants {

    String SMS = "sms";

    AuthorizationGrantType GRANT_TYPE_SMS = new AuthorizationGrantType(SMS);

    String SMS_PARAMETER_NAME = "phone";

    String DETAILS_USER = "user_info";

    String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

    String SCOPE_IS_EMPTY = "scope_is_empty";

    String DEFAULT_ID_SUFFIX = "}";

    String PASSWORD = "password";

    AuthorizationGrantType GRANT_TYPE_PASSWORD = new AuthorizationGrantType(PASSWORD);
}
