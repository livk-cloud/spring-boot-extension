/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.core.ip2region.doamin;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * IpInfo
 * </p>
 *
 * @author livk
 */
@Data
@Accessors(chain = true)
public class IpInfo {

	private final String ip;

	private final String nation;

	private final String area;

	private final String province;

	private final String city;

	private final String operator;

	/**
	 * 将字符串解析
	 * <p>
	 * 格式 IP|nation|area|province|city|operator
	 * <p>
	 * 如数据不存在则用0标识
	 * @param info ip相关字符串
	 */
	public IpInfo(String info) {
		String[] arr = info.split("\\|");
		ip = checkData(arr[0]);
		nation = checkData(arr[1]);
		area = checkData(arr[2]);
		province = checkData(arr[3]);
		city = checkData(arr[4]);
		operator = checkData(arr[5]);
	}

	private String checkData(String data) {
		return "0".equals(data) ? null : data;
	}

}
