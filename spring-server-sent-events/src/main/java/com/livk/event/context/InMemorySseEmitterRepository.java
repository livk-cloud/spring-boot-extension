package com.livk.event.context;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * InMemory
 * </p>
 *
 * @author livk
 * @date 2023/1/4
 */
@Component
public class InMemorySseEmitterRepository implements SseEmitterRepository<String> {

    private static final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    public Map<String, SseEmitter> all() {
        return sseEmitters;
    }

    @Override
    public SseEmitter get(String id) {
        return sseEmitters.get(id);
    }

    @Override
    public SseEmitter put(String id, SseEmitter sseEmitter) {
        return sseEmitters.put(id, sseEmitter);
    }

    @Override
    public SseEmitter remove(String id) {
        return sseEmitters.remove(id);
    }
}
