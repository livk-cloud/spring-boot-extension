package com.livk.disruptor.service.impl;

import com.livk.autoconfigure.disruptor.DisruptorEventProducer;
import com.livk.autoconfigure.disruptor.support.SpringDisruptor;
import com.livk.disruptor.event.MessageModel;
import com.livk.disruptor.service.DisruptorMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author livk
 */
@Slf4j
@Service
public class DisruptorMqServiceImpl implements DisruptorMqService {

    private final DisruptorEventProducer<MessageModel> producer;

    public DisruptorMqServiceImpl(SpringDisruptor<MessageModel> disruptor) {
        producer = new DisruptorEventProducer<>(disruptor);
    }

    @Override
    public void send(String message) {
        log.info("record the message: {}", message);
        producer.send(toMessageModel(message));
    }

    @Override
    public void batch(List<String> messages) {
        List<MessageModel> messageModels = messages.stream()
                .map(this::toMessageModel)
                .toList();
        producer.sendBatch(messageModels);
    }

    private MessageModel toMessageModel(String message) {
        return MessageModel.builder().message(message).build();
    }
}
