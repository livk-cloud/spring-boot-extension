package com.livk.boot.producer.controller;

import com.livk.boot.producer.dto.RocketDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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
        rocketMQTemplate.syncSend(topic, dto.getData(), 3000);
    }

    /**
     * rocketmq消息>异步发送
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendAsync/{topic}")
    public void sendAsyncMessage(@PathVariable("topic") String topic, @RequestBody RocketDTO dto) {
        rocketMQTemplate.asyncSend(topic, dto.getData(), new SendCallback() {
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
        rocketMQTemplate.sendOneWay(topic, dto.getData());
    }

}
