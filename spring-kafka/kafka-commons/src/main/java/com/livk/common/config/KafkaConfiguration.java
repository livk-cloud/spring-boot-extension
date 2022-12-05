package com.livk.common.config;

import com.livk.common.constant.KafkaConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * <p>
 * KafkaConfig
 * </p>
 *
 * @author livk
 */
@AutoConfiguration(after = KafkaAutoConfiguration.class)
public class KafkaConfiguration {

    @Bean
    public KafkaAdmin myKafkaAdmin(KafkaProperties kafkaProperties) {
        KafkaAdmin admin = new KafkaAdmin(kafkaProperties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

    @Bean
    public NewTopic myTopic() {
        return new NewTopic(KafkaConstant.NEW_TOPIC, 1, (short) 1);
    }

}
