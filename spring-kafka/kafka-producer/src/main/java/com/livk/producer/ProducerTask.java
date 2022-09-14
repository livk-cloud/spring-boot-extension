package com.livk.producer;

import com.livk.common.constant.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

/**
 * <p>
 * ProducerTask
 * </p>
 *
 * @author livk
 * @date 2022/5/4
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProducerTask {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void producer() {
        kafkaTemplate.send(KafkaConstant.TOPIC, UUID.randomUUID().toString());
        // 异步获取结果
        kafkaTemplate.send(KafkaConstant.NEW_TOPIC, UUID.randomUUID().toString())
                .addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("ex:{}", ex.getMessage());
                    }

                    @Override
                    public void onSuccess(SendResult<Object, Object> result) {
                        log.info("result:{}", result);
                    }
                });

        // 同步获取结果
        ListenableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(KafkaConstant.NEW_TOPIC, UUID.randomUUID().toString());
        try {
            SendResult<Object, Object> result = future.get();
            log.info("result:{}", result);
        } catch (Exception e) {
            log.error("ex:{}", e.getMessage());
        }
    }

}
