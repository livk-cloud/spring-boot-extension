package com.livk.event.context;

/**
 * <p>
 * SseEmitterRepository
 * </p>
 *
 * @author livk
 */
public interface SseEmitterRepository<K> extends SseEmitterLocator<K>, SseEmitterWriter<K> {
}
