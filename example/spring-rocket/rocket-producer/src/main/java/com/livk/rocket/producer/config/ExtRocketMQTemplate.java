package com.livk.rocket.producer.config;
import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
/**
 * 扩展多个template
 * @author laokou
 */
@ExtRocketMQTemplateConfiguration(
        group = "${rocketmq.ext.producer.group:livk-ext-producer-group}",
        nameServer = "${rocketmq.ext.name-server:laokou.org:9876}",
        retryTimesWhenSendFailed = 2,
        retryTimesWhenSendAsyncFailed = 2
)
public class ExtRocketMQTemplate extends RocketMQTemplate {
}
