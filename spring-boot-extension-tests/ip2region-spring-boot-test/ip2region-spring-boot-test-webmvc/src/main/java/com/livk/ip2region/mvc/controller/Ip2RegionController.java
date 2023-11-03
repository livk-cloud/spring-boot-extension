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

package com.livk.ip2region.mvc.controller;

import com.livk.core.ip2region.annotation.RequestIp;
import com.livk.core.ip2region.doamin.IpInfo;
import com.livk.core.ip2region.factory.RequestIpInfo;
import com.livk.core.ip2region.support.RequestIpContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * Ip2RegionController
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("ip")
@RequiredArgsConstructor
public class Ip2RegionController {

	private final RequestIpInfo requestIpInfo;

	@PostMapping
	public HttpEntity<List<IpInfo>> post(@RequestIp IpInfo ipInfo) {
		log.info("ip:{}", RequestIpContextHolder.get());
		return ResponseEntity.ok(List.of(ipInfo, requestIpInfo.currentIpInfo()));
	}
}
