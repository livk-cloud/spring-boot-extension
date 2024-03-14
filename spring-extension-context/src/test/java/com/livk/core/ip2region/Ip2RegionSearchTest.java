/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.core.ip2region;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.core.ip2region.doamin.IpInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class Ip2RegionSearchTest {

	static Ip2RegionSearch search;

	@BeforeAll
	static void init() throws IOException {
		Resource resource = new FileUrlResource("../ip/ip2region.xdb");
		Searcher searcher = Searcher.newWithBuffer(resource.getContentAsByteArray());
		search = new Ip2RegionSearch(searcher);
	}

	@Test
	void searchAsString() {
		String result = search.searchAsString("110.242.68.66");
		String str = "中国|0|河北省|保定市|联通";
		assertEquals(result, str);
	}

	@Test
	void searchAsInfo() {
		IpInfo result = search.searchAsInfo("110.242.68.66");
		assertEquals("110.242.68.66", result.getIp());
		assertEquals("中国", result.getNation());
		assertNull(result.getArea());
		assertEquals("河北省", result.getProvince());
		assertEquals("保定市", result.getCity());
		assertEquals("联通", result.getOperator());
	}

	@Test
	void searchAsJson() {
		String result = search.searchAsJson("110.242.68.66");
		JsonNode jsonNode = JsonMapperUtils.readTree(result);
		assertEquals("110.242.68.66", jsonNode.get("ip").asText());
		assertEquals("中国", jsonNode.get("nation").asText());
		assertTrue(jsonNode.has("area"));
		assertEquals("河北省", jsonNode.get("province").asText());
		assertEquals("保定市", jsonNode.get("city").asText());
		assertEquals("联通", jsonNode.get("operator").asText());
	}

}
