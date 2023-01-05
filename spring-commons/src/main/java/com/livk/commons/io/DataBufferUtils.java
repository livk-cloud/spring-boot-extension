package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * <p>
 * DataBufferUtils
 * </p>
 *
 * @author livk
 * @date 2023/1/5
 */
@UtilityClass
public class DataBufferUtils extends org.springframework.core.io.buffer.DataBufferUtils {

    public InputStream transform(DataBuffer dataBuffer) {
        byte[] array = dataBuffer.toByteBuffer().array();
        return new ByteArrayInputStream(array);
    }

    public Mono<InputStream> transform(Part part) {
        return join(part.content()).map(DataBufferUtils::transform);
    }

    public Flux<DataBuffer> transform(ByteArrayOutputStream byteArrayOutputStream) {
        return transform(byteArrayOutputStream.toByteArray());
    }

    public Flux<DataBuffer> transform(byte[] array) {
        ByteArrayResource resource = new ByteArrayResource(array);
        return read(resource, DefaultDataBufferFactory.sharedInstance, StreamUtils.BUFFER_SIZE);
    }
}
