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
import org.apache.curator.framework.api.PathAndBytesable;
import org.apache.curator.framework.api.Pathable;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

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
	public String createNode(String path, byte[] data) {
		return forPath(framework.create().creatingParentsIfNeeded(), path, data);
	}

	@Override
	public byte[] getNode(String path) {
		return forPath(framework.getData(), path);
	}

	@Override
	public String createTypeNode(CreateMode nodeType, String path, byte[] data) {
		return forPath(framework.create().creatingParentsIfNeeded().withMode(nodeType), path, data);
	}

	@Override
	public String createTypeSeqNode(CreateMode nodeType, String path, byte[] data) {
		return forPath(framework.create().creatingParentsIfNeeded().withProtection().withMode(nodeType), path, data);
	}

	@Override
	public Stat setData(String path, byte[] data) {
		return forPath(framework.setData(), path, data);
	}

	@Override
	public Stat setDataAsync(String path, byte[] data, CuratorListener listener) {
		framework.getCuratorListenable().addListener(listener);
		return setDataAsync(path, data);
	}

	/**
	 * Sets data async.
	 * @param path the path
	 * @param data the data
	 * @return the data async @ the exception
	 */
	public Stat setDataAsync(String path, byte[] data) {
		return forPath(framework.setData().inBackground(), path, data);
	}

	@Override
	public void deleteNode(String path) {
		forPath(framework.delete().deletingChildrenIfNeeded(), path);
	}

	@Override
	public List<String> watchedGetChildren(String path) {
		return forPath(framework.getChildren().watched(), path);
	}

	@Override
	public List<String> watchedGetChildren(String path, Watcher watcher) {
		return forPath(framework.getChildren().usingWatcher(watcher), path);
	}

	@Override
	public InterProcessLock getLock(String path, ZkLockType type) {
		return type.getLock(framework, path);
	}

	@Override
	public String getDistributedId(String path, byte[] data) {
		String seqNode = this.createTypeSeqNode(CreateMode.EPHEMERAL_SEQUENTIAL, path, data);
		int index = seqNode.lastIndexOf(path);
		if (index >= 0) {
			index += path.length();
			return index <= seqNode.length() ? seqNode.substring(index) : "";
		}
		return seqNode;
	}

	@Override
	public void close() {
		if (framework.getState() == CuratorFrameworkState.STARTED) {
			framework.close();
		}
	}

	private <T> T forPath(PathAndBytesable<T> able, String path, byte[] data) {
		try {
			return able.forPath(path, data);
		}
		catch (Exception e) {
			throw new CuratorException(e);
		}
	}

	private <T> T forPath(Pathable<T> able, String path) {
		try {
			return able.forPath(path);
		}
		catch (Exception e) {
			throw new CuratorException(e);
		}
	}

}
