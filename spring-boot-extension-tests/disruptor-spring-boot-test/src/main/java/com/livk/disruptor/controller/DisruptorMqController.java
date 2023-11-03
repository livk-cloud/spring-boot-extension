package com.livk.disruptor.controller;

import com.livk.disruptor.service.DisruptorMqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("msg")
@RequiredArgsConstructor
public class DisruptorMqController {

	private final DisruptorMqService disruptorMqService;

	@PostMapping
	public HttpEntity<Void> send() {
		disruptorMqService.send("消息到了，Hello world!");
		return ResponseEntity.ok().build();
	}

	@PostMapping("batch")
	public HttpEntity<Void> sendBatch() {
		List<String> messages = IntStream.range(1, 10)
			.mapToObj(value -> "消息到了," + value + "，Hello world!----producer")
			.toList();
		disruptorMqService.batch(messages);
		return ResponseEntity.ok().build();
	}
}
