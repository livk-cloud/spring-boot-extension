package com.livk.admin.server.config;

import com.livk.common.redis.supprot.LivkReactiveRedisTemplate;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.eventstore.ConcurrentMapEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * RedisEventStore
 * </p>
 *
 * @author livk
 * @date 2022/2/21
 */
@Component
public class RedisEventStore extends ConcurrentMapEventStore {

    private static final String INSTANCE_EVENT_KEY = "Event";

    private final ReactiveHashOperations<String, String, List<InstanceEvent>> hashOperations;

    @Autowired
    public RedisEventStore(LivkReactiveRedisTemplate reactiveRedisTemplate) {
        this(reactiveRedisTemplate, 100);
    }

    public RedisEventStore(LivkReactiveRedisTemplate reactiveRedisTemplate, int maxLogSizePerAggregate) {
        super(maxLogSizePerAggregate, new ConcurrentHashMap<>());
        hashOperations = reactiveRedisTemplate.opsForHash();
    }

    @Nonnull
    @Override
    public Mono<Void> append(List<InstanceEvent> events) {
        if (events.isEmpty()) {
            return Mono.empty();
        }
        InstanceId id = events.get(0).getInstance();
        if (!events.stream().map(InstanceEvent::getInstance).allMatch(id::equals)) {
            throw new IllegalArgumentException("events must only refer to the same instance.");
        }
        return hashOperations.put(INSTANCE_EVENT_KEY, id.getValue(), events)
                .flatMap(bool -> super.append(events).then(Mono.fromRunnable(() -> this.publish(events))));
    }

}
