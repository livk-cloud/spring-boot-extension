package com.livk.zookeeper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;

/**
 * <p>
 * LockController
 * </p>
 *
 * @author livk
 * @date 2022/4/7
 */
@Slf4j
@RestController("lock")
@RequiredArgsConstructor
public class LockController {

    private final LockRegistry lockRegistry;

    @GetMapping
    public void lock() {
        Lock lock = lockRegistry.obtain("zookeeper");
        if (lock.tryLock()) {
            try {
                log.info("is locked");
            } finally {
                lock.unlock();
            }
        }
    }
}
