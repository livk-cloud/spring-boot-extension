package com.livk.autoconfigure.curator.support;

import com.livk.autoconfigure.curator.lock.ZkLockType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * <p>
 * CuratorOperations
 * </p>
 *
 * @author livk
 * @date 2022/12/27
 */
public interface CuratorOperations {
    /**
     * 创建节点
     *
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    String createNode(String path, String data) throws Exception;

    /**
     * 创建指定类型的无序节点(持久或临时)
     *
     * @param nodeType
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    String createTypeNode(CreateMode nodeType, String path, String data) throws Exception;

    /**
     * 创建指定类型的有序节点
     *
     * @param nodeType
     * @param path
     * @param data
     * @return
     */
    String createTypeSeqNode(CreateMode nodeType, String path, String data) throws Exception;

    /**
     * 设置值
     *
     * @param path
     * @param data
     * @return
     */
    Stat setData(String path, String data) throws Exception;

    /**
     * 异步设置值
     *
     * @param path
     * @param data
     * @param listener
     * @return
     * @throws Exception
     */
    Stat setDataAsync(String path, String data, CuratorListener listener) throws Exception;

    /**
     * 删除节点
     *
     * @param path
     * @throws Exception
     */
    void deleteNode(String path) throws Exception;

    /**
     * 查看子节点
     *
     * @param path
     * @return
     * @throws Exception
     */
    List<String> watchedGetChildren(String path) throws Exception;

    /**
     * 查看子节点
     *
     * @param path
     * @param watcher
     * @return
     * @throws Exception
     */
    List<String> watchedGetChildren(String path, Watcher watcher) throws Exception;

    /**
     * 创建分布式锁
     *
     * @return
     */
    InterProcessLock getLock(String path, ZkLockType type);

    /**
     * 获取分布式ID
     *
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    String getDistributedId(String path, String data) throws Exception;
}
