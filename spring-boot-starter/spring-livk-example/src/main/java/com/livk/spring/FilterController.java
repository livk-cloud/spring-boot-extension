package com.livk.spring;

import com.livk.filter.context.TenantContextHolder;
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
public class FilterController {

    @GetMapping("tenant")
    public HttpEntity<String> tenant(@RequestHeader(TenantContextHolder.ATTRIBUTES) String tenant) {
        log.info("tenant:{}", TenantContextHolder.getTenantId());
        return ResponseEntity.ok(tenant);
    }

}
