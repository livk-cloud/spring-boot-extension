package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return read(resource, DefaultDataBufferFactory.sharedInstance, StreamUtils.BUFFER_SIZE);
    }
}
