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

package com.livk.context.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class CuratorTemplateCloseTests {

	@Test
	void closeClosesFrameworkWhenStarted() {
		CuratorFramework framework = mock(CuratorFramework.class);
		given(framework.getState()).willReturn(CuratorFrameworkState.STARTED);

		new CuratorTemplate(framework).close();

		verify(framework).close();
	}

	@Test
	void closeSkipsWhenFrameworkNotStarted() {
		CuratorFramework framework = mock(CuratorFramework.class);
		given(framework.getState()).willReturn(CuratorFrameworkState.LATENT);

		new CuratorTemplate(framework).close();

		verify(framework, never()).close();
	}

	@Test
	void createNodeWrapsException() throws Exception {
		CuratorFramework framework = mock(CuratorFramework.class, Answers.RETURNS_DEEP_STUBS);
		given(framework.create().creatingParentsIfNeeded().forPath(anyString(), any(byte[].class)))
			.willThrow(new Exception("boom"));

		assertThatThrownBy(() -> new CuratorTemplate(framework).createNode("/path", new byte[] { 1 }))
			.isInstanceOf(CuratorException.class)
			.hasCauseInstanceOf(Exception.class);
	}

	@Test
	void getDistributedIdReturnsOriginalWhenPathAbsent() throws Exception {
		CuratorFramework framework = mock(CuratorFramework.class, Answers.RETURNS_DEEP_STUBS);
		given(framework.create()
			.creatingParentsIfNeeded()
			.withProtection()
			.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
			.forPath(anyString(), any(byte[].class))).willReturn("guid-only-name");

		String id = new CuratorTemplate(framework).getDistributedId("/missing-prefix", new byte[] { 1 });

		assertThat(id).isEqualTo("guid-only-name");
	}

}
