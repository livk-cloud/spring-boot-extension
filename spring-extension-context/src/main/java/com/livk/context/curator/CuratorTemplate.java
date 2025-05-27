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

package com.livk.context.curator;

import com.livk.context.curator.lock.ZkLockType;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * CuratorTemplate
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class CuratorTemplate implements CuratorOperations {

	private final CuratorFramework framework;

	@Override
	public String createNode(String path, byte[] data) throws Exception {
		return framework.create().creatingParentsIfNeeded().forPath(path, data);
	}

	@Override
	public byte[] getNode(String path) throws Exception {
		return framework.getData().forPath(path);
	}

	@Override
	public String createTypeNode(CreateMode nodeType, String path, byte[] data) throws Exception {
		return framework.create().creatingParentsIfNeeded().withMode(nodeType).forPath(path, data);
	}

	@Override
	public String createTypeSeqNode(CreateMode nodeType, String path, byte[] data) throws Exception {
		return framework.create().creatingParentsIfNeeded().withProtection().withMode(nodeType).forPath(path, data);
	}

	@Override
	public Stat setData(String path, byte[] data) throws Exception {
		return framework.setData().forPath(path, data);
	}

	@Override
	public Stat setDataAsync(String path, byte[] data, CuratorListener listener) throws Exception {
		framework.getCuratorListenable().addListener(listener);
		return framework.setData().inBackground().forPath(path, data);
	}

	/**
	 * Sets data async.
	 * @param path the path
	 * @param data the data
	 * @return the data async
	 * @throws Exception the exception
	 */
	public Stat setDataAsync(String path, byte[] data) throws Exception {
		return framework.setData().inBackground().forPath(path, data);
	}

	@Override
	public void deleteNode(String path) throws Exception {
		framework.delete().deletingChildrenIfNeeded().forPath(path);
	}

	@Override
	public List<String> watchedGetChildren(String path) throws Exception {
		return framework.getChildren().watched().forPath(path);
	}

	@Override
	public List<String> watchedGetChildren(String path, Watcher watcher) throws Exception {
		return framework.getChildren().usingWatcher(watcher).forPath(path);
	}

	@Override
	public InterProcessLock getLock(String path, ZkLockType type) {
		return type.getLock(framework, path);
	}

	@Override
	public String getDistributedId(String path, byte[] data) throws Exception {
		String seqNode = this.createTypeSeqNode(CreateMode.EPHEMERAL_SEQUENTIAL, path, data);
		System.out.println(seqNode);
		int index = seqNode.lastIndexOf(path);
		if (index >= 0) {
			index += path.length();
			return index <= seqNode.length() ? seqNode.substring(index) : "";
		}
		return seqNode;
	}

	@Override
	public void close() throws IOException {
		if (framework.getState() == CuratorFrameworkState.STARTED) {
			framework.close();
		}
	}

}
