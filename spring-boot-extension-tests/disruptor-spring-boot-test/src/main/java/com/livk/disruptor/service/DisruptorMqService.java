package com.livk.disruptor.service;

import java.util.List;

/**
 * @author livk
 */
public interface DisruptorMqService {

	void send(String message);

	void batch(List<String> messages);

}
