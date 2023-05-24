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

package com.livk.rocket.producer.controller;

import com.livk.rocket.constant.RocketConstant;
import com.livk.rocket.dto.RocketDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
/**
 * @author laokou
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class RocketProducer {

    private final RocketMQTemplate rocketMQTemplate;

    /**
     * rocketmq消息>同步发送
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/send/{topic}")
    public void sendMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        rocketMQTemplate.syncSend(topic, dto);
    }

    /**
     * rocketmq消息>异步发送
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendAsync/{topic}")
    public void sendAsyncMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        rocketMQTemplate.asyncSend(topic, dto, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("报错信息：{}", throwable.getMessage());
            }
        });
    }

    /**
     * rocketmq消息>单向发送
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendOne/{topic}")
    public void sendOneMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        //单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
        //适用于耗时短，但对可靠性不高的场景，如日志收集
        rocketMQTemplate.sendOneWay(topic, dto);
    }

    /**
     * 事务消息
     * @param topic
     * @param dto
     */
    @PostMapping("/sendTransaction/{topic}")
    public void sendTransactionMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        Message<RocketDTO> message = MessageBuilder.withPayload(dto).setHeader(RocketMQHeaders.TRANSACTION_ID, "3").build();
        rocketMQTemplate.sendMessageInTransaction(topic,message,dto);
    }

    /**
     * 延迟消息
     * @param topic
     * @param dto
     */
    @PostMapping("/sendDelay/{topic}")
    public void sendDelay(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        // TODO delayTime 自定义
        rocketMQTemplate.syncSendDelayTimeSeconds(topic,dto,10);
    }

    /**
     * rocketmq消息>同步发送顺序消息
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendOrderly/{topic}")
    public void sendMessageOrderly(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        rocketMQTemplate.syncSendOrderly(topic, dto,RocketConstant.LIVK_MESSAGE_QUEUE_SELECTOR_KEY);
    }

    /**
     * rocketmq消息>异步发送顺序消息
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendAsyncOrderly/{topic}")
    public void sendAsyncMessageOrderly(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        rocketMQTemplate.asyncSendOrderly(topic, dto,RocketConstant.LIVK_MESSAGE_QUEUE_SELECTOR_KEY, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("报错信息：{}", throwable.getMessage());
            }
        });
    }

    /**
     * rocketmq消息>单向发送顺序消息
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendOneOrderly/{topic}")
    public void sendOneMessageOrderly(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        //单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
        //适用于耗时短，但对可靠性不高的场景，如日志收集
        rocketMQTemplate.sendOneWayOrderly(topic, dto, RocketConstant.LIVK_MESSAGE_QUEUE_SELECTOR_KEY);
    }

}
