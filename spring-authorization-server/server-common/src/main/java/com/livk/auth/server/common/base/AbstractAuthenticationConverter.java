package com.livk.auth.server.common.base;


import com.livk.auth.server.common.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * AbstractAuthenticationConverter
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
public interface AbstractAuthenticationConverter<T extends AuthenticationToken> extends AuthenticationConverter {

    boolean support(String grantType);

    default void checkParams(MultiValueMap<String, String> parameters) {

    }

    T buildToken(Authentication authentication, Set<String> scopes,
                 Map<String, Object> additionalParameters);

    @Override
    default Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (support(grantType)) {
            MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
            String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
            if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
                OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE,
                        OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
            }

            Set<String> requestedScopes = null;
            if (StringUtils.hasText(scope)) {
                requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
            }

            checkParams(parameters);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT,
                        OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
            }

            Map<String, Object> additionalParameters = parameters.entrySet()
                    .stream()
                    .filter(entry -> !entry.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) &&
                                     !entry.getKey().equals(OAuth2ParameterNames.SCOPE))
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));

            return buildToken(authentication, requestedScopes, additionalParameters);
        }
        return null;
    }
}
