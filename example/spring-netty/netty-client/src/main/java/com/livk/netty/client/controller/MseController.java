package com.livk.netty.client.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.netty.client.process.NettyClient;
import com.livk.netty.commons.protobuf.NettyMessage;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author livk
 */
@RestController
@RequestMapping("msg")
@RequiredArgsConstructor
public class MseController {

    private final NettyClient nettyClient;

    @PostMapping
    public HttpEntity<Void> send(@RequestBody JsonNode jsonNode) throws ExecutionException, InterruptedException {
        NettyMessage.Message message = NettyMessage.Message.newBuilder()
                .setType(NettyMessage.Message.MessageType.NORMAL)
                .setContent(jsonNode.toString())
                .setRequestId(UUID.randomUUID().toString()).build();
        ChannelFuture future = nettyClient.sendMsg(message);
        return ResponseEntity.ok(future.get());
    }
}
