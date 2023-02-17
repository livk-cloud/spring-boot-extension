package com.livk.pulsar.producer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.Schema;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final Producer<String> producer;

    @PostMapping
    public HttpEntity<String> send(@RequestBody JsonNode jsonNode) throws Exception {
        MessageId messageId = producer.newMessage(Schema.STRING)
                .value(jsonNode.toString())
                .sendAsync()
                .get();
        return ResponseEntity.ok(JacksonUtils.writeValueAsString(messageId));
    }
}
