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

package com.livk.core.curator;

import com.livk.core.curator.lock.ZkLockType;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
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

	private final CuratorFramework curatorFramework;

	@Override
	public String createNode(String path, String data) throws Exception {
		return curatorFramework.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
	}

	@Override
	public String createTypeNode(CreateMode nodeType, String path, String data) throws Exception {
		return curatorFramework.create().creatingParentsIfNeeded().withMode(nodeType).forPath(path, data.getBytes());
	}

	@Override
	public String createTypeSeqNode(CreateMode nodeType, String path, String data) throws Exception {
		return curatorFramework.create().creatingParentsIfNeeded().withProtection().withMode(nodeType).forPath(path, data.getBytes());
	}

	@Override
	public Stat setData(String path, String data) throws Exception {
		return curatorFramework.setData().forPath(path, data.getBytes());
	}

	@Override
	public Stat setDataAsync(String path, String data, CuratorListener listener) throws Exception {
		curatorFramework.getCuratorListenable().addListener(listener);
		return curatorFramework.setData().inBackground().forPath(path, data.getBytes());
	}

	/**
	 * Sets data async.
	 *
	 * @param path the path
	 * @param data the data
	 * @return the data async
	 * @throws Exception the exception
	 */
	public Stat setDataAsync(String path, String data) throws Exception {
		return curatorFramework.setData().inBackground().forPath(path, data.getBytes());
	}

	@Override
	public void deleteNode(String path) throws Exception {
		curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
	}

	@Override
	public List<String> watchedGetChildren(String path) throws Exception {
		return curatorFramework.getChildren().watched().forPath(path);
	}

	@Override
	public List<String> watchedGetChildren(String path, Watcher watcher) throws Exception {
		return curatorFramework.getChildren().usingWatcher(watcher).forPath(path);
	}

	@Override
	public InterProcessLock getLock(String path, ZkLockType type) {
		return switch (type) {
			case LOCK -> new InterProcessMutex(curatorFramework, path);
			case READ -> new InterProcessReadWriteLock(curatorFramework, path).readLock();
			case WRITE -> new InterProcessReadWriteLock(curatorFramework, path).writeLock();
		};
	}

	/**
	 * Gets lock.
	 *
	 * @param path the path
	 * @return the lock
	 */
	public InterProcessLock getLock(String path) {
		return getLock(path, ZkLockType.LOCK);
	}

	/**
	 * Gets read lock.
	 *
	 * @param path the path
	 * @return the read lock
	 */
	public InterProcessLock getReadLock(String path) {
		return getLock(path, ZkLockType.READ);
	}

	/**
	 * Gets write lock.
	 *
	 * @param path the path
	 * @return write lock
	 */
	public InterProcessLock getWriteLock(String path) {
		return getLock(path, ZkLockType.WRITE);
	}

	@Override
	public String getDistributedId(String path, String data) throws Exception {
		String seqNode = this.createTypeSeqNode(CreateMode.EPHEMERAL_SEQUENTIAL, "/" + path, data);
		System.out.println(seqNode);
		int index = seqNode.lastIndexOf(path);
		if (index >= 0) {
			index += path.length();
			return index <= seqNode.length() ? seqNode.substring(index) : "";
		}
		return seqNode;
	}
}
