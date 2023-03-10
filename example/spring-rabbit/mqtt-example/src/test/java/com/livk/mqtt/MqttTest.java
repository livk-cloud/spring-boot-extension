package com.livk.mqtt;

import com.livk.mqtt.handler.MqttSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * MqttTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest(classes = MqttApp.class)
public class MqttTest {

    @Autowired
    MqttSender mqttSender;

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            mqttSender.sendToMqtt("hello" + (i + 1));
        }
    }

}
