package com.livk.auth.server.support;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * <p>
 * RedisOAuth2AuthorizationConsentService
 * </p>
 *
 * @author livk
 * @date 2022/5/28
 */
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static final String OAUTH2_AUTHORIZATION_CONSENT = "OAuth2AuthorizationConsent";

    private final HashOperations<String, String, OAuth2AuthorizationConsent> opsForHash;

    public RedisOAuth2AuthorizationConsentService(RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        this.opsForHash = redisTemplate.opsForHash();
    }

    private static String getId(String registeredClientId, String principalName) {
        return String.valueOf(Objects.hash(registeredClientId, principalName));
    }

    private static String getId(OAuth2AuthorizationConsent authorizationConsent) {
        return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String id = getId(authorizationConsent);
        opsForHash.put(OAUTH2_AUTHORIZATION_CONSENT, id, authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String id = getId(authorizationConsent);
        opsForHash.delete(OAUTH2_AUTHORIZATION_CONSENT, id);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        String id = getId(registeredClientId, principalName);
        return opsForHash.get(OAUTH2_AUTHORIZATION_CONSENT, id);
    }

}
