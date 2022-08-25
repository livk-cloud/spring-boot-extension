package com.livk.redisson.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * ScheduleHandler
 * </p>
 *
 * @author livk
 * @date 2022/8/25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleHandler {

    private final RedissonClient redissonClient;

    public void start() {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("livk");
        executorService.registerWorkers(WorkerOptions.defaults());
        executorService.schedule((Runnable & Serializable) () -> log.info("66666"), 5, TimeUnit.SECONDS);
        System.out.println(executorService.getTaskIds());
    }
}
