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

package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.doamin.IpInfo;
import com.livk.commons.jackson.util.JsonMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 * <p>
 * Ip2RegionSearch
 * </p>
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class Ip2RegionSearch {

	private final Searcher searcher;

	/**
	 * Search as string.
	 *
	 * @param ip the ip
	 * @return the string
	 */
	public String searchAsString(String ip) {
		try {
			return searcher.search(ip);
		} catch (Exception e) {
			log.error("Ip2Region Searcher fail! IP:{}", ip);
			return "";
		}
	}

	/**
	 * Search as info ip info.
	 *
	 * @param ip the ip
	 * @return the ip info
	 */
	public IpInfo searchAsInfo(String ip) {
		return new IpInfo(ip + "|" + this.searchAsString(ip));
	}

	/**
	 * Search as json string.
	 *
	 * @param ip the ip
	 * @return the string
	 */
	public String searchAsJson(String ip) {
		return JsonMapperUtils.writeValueAsString(this.searchAsInfo(ip));
	}
}
