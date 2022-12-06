package com.livk.redisson.order.controller;

import com.livk.redisson.order.entity.Employer;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * OrderController
 * </p>
 *
 * @author livk
 * @date 2022/12/6
 */
@RestController
@RequestMapping("order")
public class OrderController implements DisposableBean {

    private final RDelayedQueue<Employer> delayedQueue;

    public OrderController(RedissonClient redissonClient) {
        RBlockingQueue<Employer> orderQueue = redissonClient.getBlockingQueue("order_queue");
        this.delayedQueue = redissonClient.getDelayedQueue(orderQueue);
    }

    @PostMapping("create")
    public void create() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(1L);
            Employer callCdr = new Employer();
            callCdr.setSalary(345.6);
            callCdr.setPutTime();
            delayedQueue.offer(callCdr, 30, TimeUnit.SECONDS);
        }
    }


    @Override
    public void destroy() {
        delayedQueue.destroy();
    }
}
