package com.livk.order.config;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * OrderListener
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
public class OrderListener {

    @RabbitListener(queues = RabbitConfig.dealQueueOrder)
    public void processFail(String order, Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.error("【订单号】 - [{}]被取消", order);
    }

    @RabbitListener(queues = RabbitConfig.orderQueue)
    public void processSuc(String order, Message message, Channel channel) throws IOException, InterruptedException {
        log.info("【订单号】 - [{}]", order);
        TimeUnit.SECONDS.sleep(1);
        //是否支付
        int i = RandomUtils.nextInt(1, 100);
        if (i == 6) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("支付成功！");
        } else {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            log.warn("缺少支付！");
        }
    }
}
