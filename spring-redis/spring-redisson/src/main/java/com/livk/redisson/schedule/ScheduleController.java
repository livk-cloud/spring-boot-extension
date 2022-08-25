package com.livk.redisson.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ScheduleController
 * </p>
 *
 * @author livk
 * @date 2022/8/25
 */
@RestController
@RequestMapping("schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleHandler scheduleHandler;

    @GetMapping
    public HttpEntity<String> handler() {
        scheduleHandler.start();
        return ResponseEntity.ok("ok");
    }
}
