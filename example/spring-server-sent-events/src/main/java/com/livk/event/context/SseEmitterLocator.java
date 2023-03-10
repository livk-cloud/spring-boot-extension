package com.livk.event.context;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * <p>
 * SseEmitterLocator
 * </p>
 *
 * @author livk
 */
public interface SseEmitterLocator<K> {

    Map<K, SseEmitter> all();

    SseEmitter get(K id);
}
