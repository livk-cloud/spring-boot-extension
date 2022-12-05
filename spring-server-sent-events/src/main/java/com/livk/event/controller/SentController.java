package com.livk.event.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.event.context.SentContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

/**
 * <p>
 * SentController
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
public class SentController {

    @GetMapping("/subscribe/{id}")
    public HttpEntity<SseEmitter> subscribe(@PathVariable String id) {
        SseEmitter sseEmitter = SentContext.get(id);
        if (sseEmitter == null) {
            sseEmitter = new SseEmitter(3600_000L);
            SentContext.put(id, sseEmitter);
            sseEmitter.onTimeout(() -> SentContext.remove(id));
            sseEmitter.onCompletion(() -> log.warn("推送结束！"));
        }
        return ResponseEntity.ok(sseEmitter);
    }

    @PostMapping("/push/{id}")
    public HttpEntity<Boolean> push(@PathVariable String id,
                                    @RequestBody JsonNode content) throws IOException {
        log.info("{}", content);
        SseEmitter sseEmitter = SentContext.get(id);
        if (sseEmitter == null) {
            return ResponseEntity.ok(false);
        }
        sseEmitter.send(SseEmitter.event()
                .data(content)
                .id(id));
        return ResponseEntity.ok(true);
    }

    @PostMapping("/push/data/{id}")
    public HttpEntity<Boolean> pushData(@PathVariable String id) throws IOException {
        SseEmitter sseEmitter = SentContext.get(id);
        if (sseEmitter == null) {
            return ResponseEntity.ok(false);
        }
        for (int i = 0; i < 10; i++) {
            sseEmitter.send(SseEmitter.event()
                    .data(i + ":::::" + UUID.randomUUID())
                    .id(id));
        }
        sseEmitter.complete();
        SentContext.remove(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/over/{id}")
    public HttpEntity<Boolean> over(@PathVariable String id) {
        SseEmitter sseEmitter = SentContext.get(id);
        if (sseEmitter == null) {
            return ResponseEntity.ok(false);
        }
        sseEmitter.complete();
        SentContext.remove(id);
        return ResponseEntity.ok(true);
    }
}
