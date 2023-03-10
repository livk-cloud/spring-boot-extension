package com.livk.event;

import com.livk.commons.util.DateUtils;
import com.livk.event.context.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * SentTask
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SentTask {

    private final SseEmitterRepository<String> sseEmitterRepository;

    @Scheduled(cron = "0/10 * * * * ?")
    public void push() {
        for (Map.Entry<String, SseEmitter> sseEmitterEntry : sseEmitterRepository.all().entrySet()) {
            SseEmitter sseEmitter = sseEmitterEntry.getValue();
            try {
                sseEmitter.send(SseEmitter.event()
                        .data(DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS))
                        .id(sseEmitterEntry.getKey()));
            } catch (Exception e) {
                log.error("推送异常:{}", e.getMessage());
                sseEmitter.complete();
                sseEmitterRepository.remove(sseEmitterEntry.getKey());
            }
        }
    }
}
