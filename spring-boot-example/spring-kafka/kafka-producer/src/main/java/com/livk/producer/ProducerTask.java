/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.producer;

import com.livk.common.constant.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * ProducerTask
 * </p>
 *
 * @author livk
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
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("ex:{}", throwable.getMessage());
                    } else {
                        log.info("result:{}", result);
                    }
                });

        // 同步获取结果
        CompletableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(KafkaConstant.NEW_TOPIC, UUID.randomUUID().toString());
        try {
            SendResult<Object, Object> result = future.get();
            log.info("result:{}", result);
        } catch (Exception e) {
            log.error("ex:{}", e.getMessage());
        }
    }

}
