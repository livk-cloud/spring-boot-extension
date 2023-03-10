package com.livk.spring;

import com.livk.filter.context.TenantContextHolder;
import com.livk.spring.factory.UUIDRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * FilterController
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class FilterController {

    private final UUIDRequest uuidRequest;

    @GetMapping("tenant")
    public HttpEntity<String> tenant(@RequestHeader(TenantContextHolder.ATTRIBUTES) String tenant) {
        log.info("tenant:{}", TenantContextHolder.getTenantId());
        log.info("uuid:{}", uuidRequest.currentUUID().toString());
        return ResponseEntity.ok(tenant);
    }

    @GetMapping("uuid")
    public HttpEntity<String> uuid() {
        return ResponseEntity.ok(uuidRequest.currentUUID().toString());
    }
}
