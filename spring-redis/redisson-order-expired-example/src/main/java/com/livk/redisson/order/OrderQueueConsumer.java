package com.livk.redisson.order;

import com.livk.commons.util.DateUtils;
import com.livk.redisson.order.entity.Employer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * OrderQueueConsumer
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
public class OrderQueueConsumer implements Runnable, InitializingBean {

    private final RBlockingQueue<Employer> orderQueue;

    public OrderQueueConsumer(RedissonClient redissonClient) {
        this.orderQueue = redissonClient.getBlockingQueue("order_queue");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Employer employer = orderQueue.take();
                log.info("订单取消时间：{} ==订单生成时间:{}", DateUtils.format(LocalDateTime.now(), DateUtils.HMS), employer.getPutTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        executor.execute(this);
    }

}
