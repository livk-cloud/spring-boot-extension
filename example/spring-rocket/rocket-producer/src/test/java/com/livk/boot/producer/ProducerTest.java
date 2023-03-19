package com.livk.boot.producer;

import com.livk.boot.producer.controller.RocketProducer;
import com.livk.boot.producer.dto.RocketDTO;
import com.livk.boot.rocket.constant.RocketConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author laokou
 */
@SpringBootTest
public class ProducerTest {

    @Autowired
    private RocketProducer rocketProducer;

    @Test
    public void test() {
        String msg = "Java第一，老寇无敌。千秋万代，一统江湖。";
        RocketDTO dto = new RocketDTO();
        dto.setData(msg);
        rocketProducer.sendAsyncMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
        rocketProducer.sendOneMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
        rocketProducer.sendMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
    }

    @Test
    public void txTest() {
        String msg = "Java第一，老寇无敌。千秋万代，一统江湖。";
        RocketDTO dto = new RocketDTO();
        dto.setData(msg);
        rocketProducer.sendTransactionMessage(RocketConstant.LIVK_MESSAGE_TOPIC,dto);
    }

}
