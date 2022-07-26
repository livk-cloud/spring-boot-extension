package com.livk.pulsar.common;

import com.livk.pulsar.common.properties.PulsarProperties;
import org.apache.pulsar.client.api.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * PulsarAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/4/27
 */
@ConditionalOnClass(PulsarClient.class)
@AutoConfiguration
@EnableConfigurationProperties(PulsarProperties.class)
public class PulsarAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean(destroyMethod = "close")
    public PulsarClient pulsarClient(PulsarProperties properties) throws PulsarClientException {
        return PulsarClient.builder().serviceUrl(properties.getAddress()).build();
    }

    @ConditionalOnMissingBean
    @Bean(destroyMethod = "close")
    public Producer<String> producer(PulsarClient client, PulsarProperties properties) throws PulsarClientException {
        return client.newProducer(Schema.STRING).topic(properties.getTopic()).compressionType(CompressionType.LZ4)
                .sendTimeout(0, TimeUnit.SECONDS).enableBatching(true)
                .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS).batchingMaxMessages(1000).maxPendingMessages(1000)
                .blockIfQueueFull(true).roundRobinRouterBatchingPartitionSwitchFrequency(10)
                .batcherBuilder(BatcherBuilder.DEFAULT).create();
    }

    @ConditionalOnMissingBean
    @Bean(destroyMethod = "close")
    public Consumer<String> consumer(PulsarClient client, PulsarProperties properties) throws PulsarClientException {
        return client.newConsumer(Schema.STRING).topic(properties.getTopic()).subscriptionName("livk")
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                .negativeAckRedeliveryDelay(60, TimeUnit.SECONDS).receiverQueueSize(1000).subscribe();
    }

}
