package com.livk.doc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * DocController
 * </p>
 *
 * @author livk
 * @date 2022/7/23
 */
@RestController
@RequestMapping("doc")
@RequiredArgsConstructor
public class DocController {

    private final ObjectMapper objectMapper;

    @GetMapping
    public HttpEntity<String> get(@RequestParam String name) {
        return ResponseEntity.ok("hello " + name);
    }

    @PostMapping
    public HttpEntity<JsonNode> post(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.readTree(objectMapper.writeValueAsString(data)));
    }
}
