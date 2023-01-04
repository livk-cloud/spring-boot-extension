package com.livk.event.context;

/**
 * <p>
 * SseEmitterRepository
 * </p>
 *
 * @author livk
 * @date 2023/1/4
 */
public interface SseEmitterRepository<K> extends SseEmitterLocator<K>, SseEmitterWriter<K> {
}
