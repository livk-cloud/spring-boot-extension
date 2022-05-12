package com.livk.spring;

import com.livk.filter.context.TenantContext;
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
 * @date 2022/5/10
 */
@Slf4j
@RestController
public class FilterController {

	@GetMapping("tenant")
	public HttpEntity<String> tenant(@RequestHeader(TenantContext.ATTRIBUTES) String tenant) {
		log.info("tenant:{}", TenantContext.getTenantId());
		return ResponseEntity.ok(tenant);
	}

}
