/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.web.multipart;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.io.FileUtils;
import com.livk.commons.jackson.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
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
        assertEquals("input.json", part.filename());
        assertEquals("input.json", part.name());
        assertEquals(new HttpHeaders(), part.headers());

        File file = new File(System.getProperty("user.dir") + "/input.json");
        StepVerifier.create(part.transferTo(file))
                .verifyComplete();

        ByteArrayInputStream stream = new ByteArrayInputStream(FileUtils.copyToByteArray(file));

        JsonNode jsonNode = JacksonUtils.readValue(resource.getInputStream(), JsonNode.class);

        JsonNode newFileNode = JacksonUtils.readValue(stream, JsonNode.class);

        assertEquals(jsonNode, newFileNode);
        assertTrue(file.delete());

        Mono<JsonNode> mono = DataBufferUtils.transformByte(part.content())
                .publishOn(Schedulers.boundedElastic())
                .map(String::new)
                .map(JacksonUtils::readTree);
        StepVerifier.create(mono)
                .expectNext(jsonNode)
                .verifyComplete();
    }
}
