package com.livk.auth.server.support;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * <p>
 * RedisRegisteredClientRepository
 * </p>
 *
 * @author livk
 * @date 2022/5/28
 */
public class RedisRegisteredClientRepository implements RegisteredClientRepository {

    private static final String CLIENT_KEY = "RegisteredClient";

    private final HashOperations<String, String, RegisteredClient> opsForHash;

    public RedisRegisteredClientRepository(RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        this.opsForHash = redisTemplate.opsForHash();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        opsForHash.put(CLIENT_KEY, getId(registeredClient.getClientId()), registeredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return opsForHash.get(CLIENT_KEY, id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return findById(getId(clientId));
    }

    /**
     * {@link RegisteredClient#getId()}与{@link RegisteredClient#getClientId()}关系对应
     *
     * @param clientId clientId
     * @return Id
     */
    public String getId(String clientId) {
        return UUID.nameUUIDFromBytes(clientId.getBytes()).toString();
    }

}
