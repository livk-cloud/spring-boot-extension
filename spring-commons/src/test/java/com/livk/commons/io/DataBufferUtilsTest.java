package com.livk.commons.io;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.IOException;

/**
 * @author livk
 */
class DataBufferUtilsTest {

    @Test
    void transform() {
        String text = "123456";
        Flux<DataBuffer> bufferFlux = DataBufferUtils.transform(text.getBytes());
        Mono<String> mono = DataBufferUtils.transform(bufferFlux)
                .publishOn(Schedulers.boundedElastic())
                .map(inputStream -> {
                    try {
                        return new String(inputStream.readAllBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        StepVerifier.create(mono)
                .expectNext(text)
                .verifyComplete();
    }
}
