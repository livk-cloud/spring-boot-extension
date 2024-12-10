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

package com.livk.context.fastexcel.listener;

import com.livk.context.fastexcel.FastExcelSupport;
import com.livk.context.fastexcel.Info;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author livk
 */
class DefaultExcelMapReadListenerTest {

	static DefaultExcelMapReadListener listener = new DefaultExcelMapReadListener();

	@Test
	void test() throws IOException {
		InputStream inputStream = new ClassPathResource("outFile.xls").getInputStream();
		FastExcelSupport.read(inputStream, Info.class, listener, true);

		Map<String, ? extends Collection<Object>> map = listener.getMapData();
		assertFalse(map.isEmpty());

		assertEquals(1, map.size());
		assertEquals(Set.of("0"), map.keySet());
		assertEquals(100, map.values().stream().mapToLong(Collection::size).sum());
	}

}
