package com.livk.provider.controller;

import com.livk.amqp.entity.Message;
import com.livk.provider.send.RabbitSend;
import com.livk.commons.util.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * RabbitController
 * </p>
 *
 * @author livk
 * @date 2022/4/14
 */
@RestController
@RequestMapping("provider")
@RequiredArgsConstructor
public class RabbitController {

    private final RabbitSend send;

    @PostMapping("sendMsgDirect")
    public <T> void sendMsgDirect(@RequestBody Message<T> message) {
        send.sendMsgDirect(message);
    }

    @PostMapping("sendMsgFanout")
    public <T> void sendMsgFanout(@RequestBody Message<T> message) {
        send.sendMsgFanout(message);
    }

    @PostMapping("/sendMsgTopic/{key}")
    public <T> void sendMsgTopic(@RequestBody Message<T> message, @PathVariable String key) {
        send.sendMsgTopic(message, key);
    }

    @PostMapping("sendMsgHeaders")
    public <T> void sendMsgHeaders(@RequestBody Message<T> message, @RequestParam String json) {
        send.sendMsgHeaders(message, JacksonUtils.toMap(json, String.class, Object.class));
    }

}
