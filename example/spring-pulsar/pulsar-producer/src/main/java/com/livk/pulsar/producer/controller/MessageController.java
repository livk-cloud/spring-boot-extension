package com.livk.pulsar.producer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JacksonUtils;
import com.livk.pulsar.common.entity.PulsarMessage;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Schema;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * MessageController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("producer")
@RequiredArgsConstructor
public class MessageController {

    private final PulsarTemplate<String> pulsarTemplate;

    @PostMapping
    public HttpEntity<String> send(@RequestBody JsonNode jsonNode) throws Exception {
        PulsarMessage<JsonNode> message = new PulsarMessage<>();
        message.setMsgId(UUID.randomUUID().toString());
        message.setSendTime(LocalDateTime.now());
        message.setData(jsonNode);

        MessageId messageId = pulsarTemplate.sendAsync(message.toJson(), Schema.STRING).get();
        return ResponseEntity.ok(JacksonUtils.writeValueAsString(messageId));
    }
}
