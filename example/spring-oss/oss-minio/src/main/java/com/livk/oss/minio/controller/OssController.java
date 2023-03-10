package com.livk.oss.minio.controller;

import com.livk.autoconfigure.oss.support.OSSTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author livk
 */
@RestController
@RequestMapping("oss")
@RequiredArgsConstructor
public class OssController {

    public final OSSTemplate ossTemplate;

    @PostConstruct
    public void init() {
        ossTemplate.removeBucketAndObj("test");
    }

    @GetMapping("test")
    public HttpEntity<String> test() throws IOException {
        InputStream stream = new ClassPathResource("test.jpg").getInputStream();
        ossTemplate.createBucket("test");
        if (!ossTemplate.exist("test", "test.jpg")) {
            ossTemplate.upload("test", "test.jpg", stream);
        }
        String url = ossTemplate.getExternalLink("test", "test.jpg");
        return ResponseEntity.ok(url);
    }
}
