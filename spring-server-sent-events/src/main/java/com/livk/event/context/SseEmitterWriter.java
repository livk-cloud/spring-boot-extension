package com.livk.event.context;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * <p>
 * SseEmitterWriter
 * </p>
 *
 * @author livk
 */
public interface SseEmitterWriter<K> {

    SseEmitter put(K id, SseEmitter sseEmitter);

    SseEmitter remove(K id);
}
