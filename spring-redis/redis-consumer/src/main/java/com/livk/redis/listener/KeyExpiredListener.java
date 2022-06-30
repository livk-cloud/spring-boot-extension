package com.livk.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * KeyExpiredListener
 * </p>
 *
 * @author livk
 * @date 2022/3/17
 */
@Slf4j
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        var key = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("key:<{}>", key);
    }

}
