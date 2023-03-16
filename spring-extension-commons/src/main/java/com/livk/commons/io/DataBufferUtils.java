package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * DataBufferUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class DataBufferUtils extends org.springframework.core.io.buffer.DataBufferUtils {

    /**
     * The constant BUFFER_SIZE.
     */
    public static final int BUFFER_SIZE = StreamUtils.BUFFER_SIZE;

    /**
     * The constant DEFAULT_FACTORY.
     */
    public static final DataBufferFactory DEFAULT_FACTORY = DefaultDataBufferFactory.sharedInstance;

    /**
     * Transform mono.
     *
     * @param dataBufferFlux the data buffer flux
     * @return the mono
     */
    public Mono<InputStream> transform(Flux<DataBuffer> dataBufferFlux) {
        return join(dataBufferFlux).map(DataBuffer::asInputStream);
    }

    /**
     * Transform flux.
     *
     * @param array the array
     * @return the flux
     */
    public Flux<DataBuffer> transform(byte[] array) {
        ByteArrayResource resource = new ByteArrayResource(array);
        return read(resource, DEFAULT_FACTORY, BUFFER_SIZE);
    }

    /**
     * Transform byte mono.
     *
     * @param bufferFlux the buffer flux
     * @return the mono
     */
    public Mono<byte[]> transformByte(Flux<DataBuffer> bufferFlux) {
        return DataBufferUtils.transform(bufferFlux)
                .publishOn(Schedulers.boundedElastic())
                .map(inputStream -> {
                    try {
                        return inputStream.readAllBytes();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Transform flux.
     *
     * @param inputStream the input stream
     * @return the flux
     */
    public Flux<DataBuffer> transform(InputStream inputStream) {
        return readInputStream(() -> inputStream, DEFAULT_FACTORY, BUFFER_SIZE);
    }

    /**
     * Transform flux.
     *
     * @param inputStreamMono the input stream mono
     * @return the flux
     */
    public Flux<DataBuffer> transform(Mono<InputStream> inputStreamMono) {
        return inputStreamMono.flatMapMany(DataBufferUtils::transform);
    }
}
