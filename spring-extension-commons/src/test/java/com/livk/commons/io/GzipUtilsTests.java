/*
 * Copyright 2021-present the original author or authors.
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
 */

package com.livk.commons.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class GzipUtilsTests {

	@Test
	void compressAndDecompressRoundTrip() throws IOException {
		byte[] payload = "hello-gzip".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream compressed = new ByteArrayOutputStream();
		GzipUtils.compress(payload, compressed);

		byte[] restored = GzipUtils.decompress(new ByteArrayInputStream(compressed.toByteArray()));
		assertThat(restored).isEqualTo(payload);
	}

	@Test
	void compressSkipsEmptyBytes() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		GzipUtils.compress(new byte[0], outputStream);
		GzipUtils.compress(null, outputStream);

		assertThat(outputStream.toByteArray()).isEmpty();
	}

	@Test
	void decompressRejectsInvalidGzip() {
		assertThatThrownBy(() -> GzipUtils.decompress(new ByteArrayInputStream("not-gzip".getBytes())))
			.isInstanceOf(ZipException.class);
	}

}
