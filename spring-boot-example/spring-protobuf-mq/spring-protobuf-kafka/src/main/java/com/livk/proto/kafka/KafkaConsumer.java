package com.livk.proto.kafka;

import com.livk.proto.ConsumerCheck;
import com.livk.proto.User;
import com.livk.proto.kafka.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(id = "consumer", topics = KafkaConfig.TOPIC_NAME)
    public void consumer(ConsumerRecord<String, User> record) {
        log.info("topic[{}],offset[{}],partition[{}],key[{}],val[{}]",
                record.topic(), record.offset(), record.partition(), record.key(), record.value());
		ConsumerCheck.success();
    }
}
