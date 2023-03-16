package com.livk.commons.web.multipart;

import com.livk.commons.io.DataBufferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Callable;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class ResourceFilePart implements FilePart {

    private final Resource resource;

    @Override
    public String filename() {
        return resource.getFilename();
    }

    @Override
    public Mono<Void> transferTo(Path dest) {
        return blockingOperation(() ->
                Files.copy(resource.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING));
    }

    private Mono<Void> blockingOperation(Callable<?> callable) {
        return Mono.<Void>create(sink -> {
            try {
                callable.call();
                sink.success();
            } catch (Exception ex) {
                sink.error(ex);
            }
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public String name() {
        return resource.getFilename();
    }

    @Override
    public HttpHeaders headers() {
        return new HttpHeaders();
    }

    @Override
    public Flux<DataBuffer> content() {
        return DataBufferUtils.read(resource, DefaultDataBufferFactory.sharedInstance, StreamUtils.BUFFER_SIZE);
    }
}
