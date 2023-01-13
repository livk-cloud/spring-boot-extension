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
 */
public interface CuratorOperations {
    /**
     * 创建节点
     *
     * @param path the path
     * @param data the data
     * @return string
     * @throws Exception the exception
     */
    String createNode(String path, String data) throws Exception;

    /**
     * 创建指定类型的无序节点(持久或临时)
     *
     * @param nodeType the node type
     * @param path     the path
     * @param data     the data
     * @return string
     * @throws Exception the exception
     */
    String createTypeNode(CreateMode nodeType, String path, String data) throws Exception;

    /**
     * 创建指定类型的有序节点
     *
     * @param nodeType the node type
     * @param path     the path
     * @param data     the data
     * @return string
     * @throws Exception the exception
     */
    String createTypeSeqNode(CreateMode nodeType, String path, String data) throws Exception;

    /**
     * 设置值
     *
     * @param path the path
     * @param data the data
     * @return data
     * @throws Exception the exception
     */
    Stat setData(String path, String data) throws Exception;

    /**
     * 异步设置值
     *
     * @param path     the path
     * @param data     the data
     * @param listener the listener
     * @return data async
     * @throws Exception the exception
     */
    Stat setDataAsync(String path, String data, CuratorListener listener) throws Exception;

    /**
     * 删除节点
     *
     * @param path the path
     * @throws Exception the exception
     */
    void deleteNode(String path) throws Exception;

    /**
     * 查看子节点
     *
     * @param path the path
     * @return list
     * @throws Exception the exception
     */
    List<String> watchedGetChildren(String path) throws Exception;

    /**
     * 查看子节点
     *
     * @param path    the path
     * @param watcher the watcher
     * @return list
     * @throws Exception the exception
     */
    List<String> watchedGetChildren(String path, Watcher watcher) throws Exception;

    /**
     * 创建分布式锁
     *
     * @param path the path
     * @param type the type
     * @return lock
     */
    InterProcessLock getLock(String path, ZkLockType type);

    /**
     * 获取分布式ID
     *
     * @param path the path
     * @param data the data
     * @return distributed id
     * @throws Exception the exception
     */
    String getDistributedId(String path, String data) throws Exception;
}
