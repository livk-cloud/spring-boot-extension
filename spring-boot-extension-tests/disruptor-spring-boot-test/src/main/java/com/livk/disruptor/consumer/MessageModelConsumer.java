package com.livk.disruptor.consumer;

import com.livk.core.disruptor.DisruptorEventConsumer;
import com.livk.disruptor.event.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
public class MessageModelConsumer implements DisruptorEventConsumer<MessageModel> {

	@Override
	public void onEvent(MessageModel event, long sequence, boolean endOfBatch) throws Exception {
		log.info("消费者消费的信息是：{} :{} :{}", event, sequence, endOfBatch);
	}

}
