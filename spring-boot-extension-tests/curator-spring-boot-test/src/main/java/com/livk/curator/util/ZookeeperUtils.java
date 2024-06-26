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

package com.livk.curator.util;

import com.livk.commons.SpringContextHolder;
import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * ZookeeperUtils
 * </p>
 *
 * @author livk
 */
@Slf4j
@UtilityClass
public class ZookeeperUtils {

	private static final CuratorFramework curatorFramework = SpringContextHolder.getBean(CuratorFramework.class);

	/**
	 * 路径分隔符
	 */
	private static final String PATH_SEPARATOR = "/";

	/**
	 * 创建空节点，默认持久节点
	 * @param path 节点路径
	 * @param node 节点名称
	 * @return 完整路径
	 */
	public String createNode(String path, String node) {
		return createNode(path, node, CreateMode.PERSISTENT);
	}

	/**
	 * 创建带类型的空节点
	 * @param path 节点路径
	 * @param node 节点名称
	 * @param createMode 类型 CreateMode.PERSISTENT: 创建节点后，不删除就永久存在
	 * CreateMode.PERSISTENT_SEQUENTIAL：节点path末尾会追加一个10位数的单调递增的序列
	 * CreateMode.EPHEMERAL：创建后，回话结束节点会自动删除
	 * CreateMode.EPHEMERAL_SEQUENTIAL：节点path末尾会追加一个10位数的单调递增的序列
	 * @return 路径
	 */
	public String createNode(String path, String node, CreateMode createMode) {
		path = buildPath(path, node);
		log.info("create node for path: {} with createMode: {}", path, createMode.name());
		try {
			curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path);
			log.info("create node :{} successfully", node);
			return path;
		}
		catch (Exception e) {
			log.error("create node for path: {} with createMode: {} failed!", path, createMode.name(), e);
			return null;
		}
	}

	/**
	 * 创建节点，默认持久节点
	 * @param path 节点路径
	 * @param node 节点名称
	 * @param value 节点值
	 * @return 完整路径
	 */
	public String createNode(String path, String node, String value) {
		return createNode(path, node, value, CreateMode.PERSISTENT);
	}

	/**
	 * 创建节点，默认持久节点
	 * @param path 节点路径
	 * @param node 节点名称
	 * @param value 节点值
	 * @param createMode 节点类型
	 * @return 完整路径
	 */
	public String createNode(String path, String node, String value, CreateMode createMode) {
		if (Objects.isNull(value)) {
			log.error("ZooKeeper节点值不能为空!");
		}
		path = buildPath(path, node);
		log.info("create node for path: {}, value: {}, with createMode: {}", path, value, createMode.name());
		try {
			curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, value.getBytes());
			return path;
		}
		catch (Exception e) {
			log.error("create node for path: {}, value: {}, with createMode: {} failed!", path, value,
					createMode.name(), e);
		}
		return null;
	}

	/**
	 * 获取节点数据
	 * @param path 路径
	 * @param node 节点名称
	 * @return 完整路径
	 */
	public String get(String path, String node) {
		path = buildPath(path, node);
		try {
			byte[] bytes = curatorFramework.getData().forPath(path);
			if (bytes.length > 0) {
				return new String(bytes);
			}
		}
		catch (Exception e) {
			log.error("get value for path: {}, node: {} failed!", path, node, e);
		}
		return null;
	}

	/**
	 * 更新节点数据
	 * @param path 节点路径
	 * @param node 节点名称
	 * @param value 更新值
	 * @return 完整路径
	 */
	public String update(String path, String node, String value) {
		if (Objects.isNull(value)) {
			log.error("ZooKeeper节点值不能为空!");
		}
		path = buildPath(path, node);
		log.info("update path: {} to value: {}", path, value);

		try {
			curatorFramework.setData().forPath(path, value.getBytes());
			return path;
		}
		catch (Exception e) {
			log.error("update path: {} to value: {} failed!", path, value);
		}
		return null;
	}

	/**
	 * 删除节点，并且递归删除子节点
	 * @param path 路径
	 * @param node 节点名称
	 * @return 路径
	 */
	public boolean delete(String path, String node) {
		path = buildPath(path, node);
		log.info("delete node for path: {}", path);

		try {
			curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
			return true;
		}
		catch (Exception e) {
			log.error("delete node for path: {} failed!", path);
		}
		return false;
	}

	/**
	 * 获取子节点
	 * @param path 节点路径
	 * @return
	 */
	public List<String> getChildren(String path) {
		if (StringUtils.hasText(path)) {
			if (!path.startsWith(PATH_SEPARATOR)) {
				path = PATH_SEPARATOR + path;
			}

			try {
				return curatorFramework.getChildren().forPath(path);
			}
			catch (Exception e) {
				log.error("get children path:{} error", path, e);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * 判断节点是否存在
	 * @param path 路径
	 * @param node 节点名称
	 * @return 结果
	 */
	public boolean exists(String path, String node) {
		try {
			List<String> list = getChildren(path);
			return !CollectionUtils.isEmpty(list) && list.contains(node);
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 申请锁，指定请求等待时间
	 * @param path 加锁zk节点
	 * @param time 时间
	 * @param unit 时间单位
	 * @param runnable 执行方法
	 */
	public void lock(String path, long time, TimeUnit unit, Runnable runnable) {
		try {
			InterProcessMutex lock = new InterProcessMutex(curatorFramework, path);
			if (lock.acquire(time, unit)) {
				try {
					runnable.run();
				}
				finally {
					lock.release();
				}
			}
			else {
				log.error("获取锁超时：{}!", path);
			}
		}
		catch (Exception e) {
			log.error("获取锁失败: {}!", path);
		}
	}

	/**
	 * 申请锁，指定请求等待时间
	 * @param path 加锁zk节点
	 * @param time 时间
	 * @param unit 时间单位
	 * @param callable 执行方法
	 * @return .
	 */
	public <T> T lock(String path, long time, TimeUnit unit, Callable<T> callable) {
		try {
			InterProcessMutex lock = new InterProcessMutex(curatorFramework, path);
			if (lock.acquire(time, unit)) {
				try {
					return callable.call();
				}
				finally {
					lock.release();
				}
			}
			else {
				log.error("获取锁超时：{}!", path);
			}
		}
		catch (Exception e) {
			log.error("获取锁失败: {}!", path);
		}
		return null;
	}

	/* */

	/**
	 * 对一个节点进行监听，监听事件包括指定的路径节点的增、删、改的操作
	 * @param path 节点路径
	 */

	public void watchNode(String path, boolean dataIsCompressed) {
		try {
			CuratorCache curatorCache = CuratorCache.build(curatorFramework, path);
			curatorCache.listenable().addListener((type, oldData, data) -> {
				log.info("ZNode节点状态改变, path={}", data.getPath());
				log.info("ZNode节点状态改变, data={}", data.getData());
				log.info("ZNode节点状态改变, stat={}", data.getStat());
			});
			curatorCache.start();
		}
		catch (Exception e) {
			log.error("创建NodeCache监听失败, path={}", path);
		}
	}

	/**
	 * 对指定的路径节点的一级子目录进行监听，不对该节点的操作进行监听，对其子目录的节点进行增、删、改的操作监听
	 * @param path 节点路径
	 * @param listener 回调方法
	 */
	public void watchChildren(String path, CuratorCacheListener listener) {
		try {
			CuratorCache curatorCache = CuratorCache.build(curatorFramework, path);
			curatorCache.start();
			curatorCache.listenable().addListener(listener);
		}
		catch (Exception e) {
			log.error("watch children failed for path: {}", path, e);
		}
	}

	/**
	 * 将指定的路径节点作为根节点（祖先节点），对其所有的子节点操作进行监听，呈现树形目录的监听，可以设置监听深度，最大监听深度为2147483647（ int类型的最大值）
	 * @param path 节点路径
	 * @param maxDepth 回调方法
	 * @param listener 监听
	 */
	public void watchTree(String path, int maxDepth, CuratorCacheListener listener) {
		try {
			CuratorCache curatorCache = CuratorCache.build(curatorFramework, path);
			curatorCache.start();
			curatorCache.listenable().addListener(listener);
		}
		catch (Exception e) {
			log.error("watch tree failed for path: {}", path, e);
		}
	}

	public String buildPath(String path, String node) {
		if (ObjectUtils.isEmpty(path) || ObjectUtils.isEmpty(node)) {
			log.error("ZooKeeper路径或者节点名称不能为空！");
		}

		if (!path.startsWith(PATH_SEPARATOR)) {
			path = PATH_SEPARATOR + path;
		}

		if (PATH_SEPARATOR.equals(path)) {
			return path + node;
		}
		else {
			return path + PATH_SEPARATOR + node;
		}
	}

}
