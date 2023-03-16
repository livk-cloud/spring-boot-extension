package com.livk.commons.web.multipart;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.io.FileUtils;
import com.livk.commons.jackson.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class ResourceFilePartTest {
    @Test
    public void test() throws IOException {
        ClassPathResource resource = new ClassPathResource("input.json");
        ResourceFilePart part = new ResourceFilePart(resource);
        System.out.println(part.filename());
        System.out.println(part.name());
        System.out.println(part.headers());

        File file = new File(System.getProperty("user.dir") + "/input.json");
        StepVerifier.create(part.transferTo(file))
                .verifyComplete();

        ByteArrayInputStream stream = new ByteArrayInputStream(FileUtils.copyToByteArray(file));

        JsonNode jsonNode = JacksonUtils.readValue(resource.getInputStream(), JsonNode.class);

        JsonNode newFileNode = JacksonUtils.readValue(stream, JsonNode.class);

        assertEquals(jsonNode, newFileNode);
        assertTrue(file.delete());

        Mono<JsonNode> mono = DataBufferUtils.transform(part.content())
                .publishOn(Schedulers.boundedElastic())
                .map(inputStream -> {
                    try {
                        return new String(inputStream.readAllBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).map(JacksonUtils::readTree);
        StepVerifier.create(mono)
                .expectNext(jsonNode)
                .verifyComplete();
    }
}
