package com.livk.event.context;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * SentContext
 * </p>
 *
 * @author livk
 */
public class SentContext {
    private static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();

    public static SseEmitter get(String id) {
        return SSE_CACHE.get(id);
    }

    public static SseEmitter put(String id, SseEmitter sseEmitter) {
        return SSE_CACHE.put(id, sseEmitter);
    }

    public static void remove(String id) {
        SSE_CACHE.remove(id);
    }

    public static Map<String, SseEmitter> all() {
        return SSE_CACHE;
    }
}
