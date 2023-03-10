package com.livk.redisson.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScheduledFuture;
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
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleHandler {

    private final RedissonClient redissonClient;

    public void start() {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("livk");
        executorService.registerWorkers(WorkerOptions.defaults().workers(12));
        RScheduledFuture<?> future = executorService.scheduleAtFixedRate((Runnable & Serializable) () -> {
            log.info("time:{}", System.currentTimeMillis());
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.SECONDS);
        log.info("TaskId:{}", future.getTaskId());
//        executorService.delete();
    }
}
