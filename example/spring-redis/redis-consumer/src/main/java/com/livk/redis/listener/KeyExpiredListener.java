package com.livk.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * KeyExpiredListener
 * </p>
 *
 * @author livk
 */
@Slf4j
public class KeyExpiredListener extends KeyspaceEventMessageListener {

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doHandleMessage(Message message) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("key:<{}>", key);
    }

}
