/**
 * Copyright (c) 2022 KCloud-Platform-Official Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.boot.producer.controller;

import com.livk.boot.producer.dto.RocketDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kou Shenhai
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class RocketProducer {

   private final RocketMQTemplate rocketMQTemplate;

    @PostMapping("/send/{topic}")
    /**
     * rocketmq消息>同步发送
     */
    public void sendMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        rocketMQTemplate.syncSend(topic,dto.getData(),3000);
    }

    @PostMapping("/sendAsync/{topic}")
    /**
     * rocketmq消息>异步发送
     */
    public void sendAsyncMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        rocketMQTemplate.asyncSend(topic, dto.getData(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("报错信息：{}",throwable.getMessage());
            }
        });
    }

    @PostMapping("/sendOne/{topic}")
    /**
     * rocketmq消息>单向发送
     */
    public void sendOneMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        /**
         * 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
         * 适用于耗时短，但对可靠性不高的场景，如日志收集
         */
        rocketMQTemplate.sendOneWay(topic,dto.getData());
    }

}
