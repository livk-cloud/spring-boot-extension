package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.Part;
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
     * @param part the part
     * @return the mono
     */
    public Mono<InputStream> transform(Part part) {
        return join(part.content()).map(DataBuffer::asInputStream);
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
