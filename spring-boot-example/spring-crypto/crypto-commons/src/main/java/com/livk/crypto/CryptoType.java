/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.crypto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author livk
 */
@Getter
@RequiredArgsConstructor
public enum CryptoType {

	AES("AES"),

	PBE("PBE");

	private final String algorithm;

	public static CryptoType match(String str) {
		for (CryptoType type : values()) {
			if (str.startsWith(type.getAlgorithm())) {
				return type;
			}
		}
		throw new IllegalArgumentException("未匹配到CryptoType str:" + str);
	}

	public String wrapper(String str) {
		return checkWrapper(str) ? str : algorithm + "(" + str + ")";
	}

	public String unwrap(String str) {
		if (checkWrapper(str)) {
			str = str.replaceFirst(algorithm + "\\(", "");
			int length = str.length();
			return str.substring(0, length - 1);
		}
		else {
			return str;
		}
	}

	private boolean checkWrapper(String str) {
		return str.startsWith(algorithm + "(") && str.endsWith(")");
	}

}
