package com.livk.zookeeper.util;

import com.livk.common.SpringContextHolder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * ZookeeperUtils
 * </p>
 *
 * @author livk
 * @date 2022/4/6
 */
@Slf4j
@UtilityClass
public class ZookeeperUtils {

    private static final ZooKeeper zooKeeper = SpringContextHolder.getBean(ZooKeeper.class);

    /**
     * 判断指定节点是否存在
     *
     * @param path      path
     * @param needWatch 指定是否复用zookeeper中默认的Watcher
     * @return stat
     */
    public Stat exists(String path, boolean needWatch) {
        try {
            return zooKeeper.exists(path, needWatch);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}", path, e);
            return null;
        }
    }

    /**
     * 检测结点是否存在 并设置监听事件 三种监听类型： 创建，删除，更新
     *
     * @param path    path
     * @param watcher 传入指定的监听类
     * @return stat
     */
    public Stat exists(String path, Watcher watcher) {
        try {
            return zooKeeper.exists(path, watcher);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}", path, e);
            return null;
        }
    }

    /**
     * 创建持久化节点
     *
     * @param path path
     * @param data data
     * @return boolean
     */
    public boolean createNode(String path, String data) {
        try {
            zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        } catch (Exception e) {
            log.error("【创建持久化节点异常】{},{},{}", path, data, e);
            return false;
        }
    }

    /**
     * 修改持久化节点
     *
     * @param path path
     * @param data data
     * @return boolean
     */
    public boolean updateNode(String path, String data) {
        try {
            // zk的数据版本是从0开始计数的。如果客户端传入的是-1，则表示zk服务器需要基于最新的数据进行更新。如果对zk的数据节点的更新操作没有原子性要求则可以使用-1.
            // version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zooKeeper.setData(path, data.getBytes(), -1);
            return true;
        } catch (Exception e) {
            log.error("【修改持久化节点异常】{},{},{}", path, data, e);
            return false;
        }
    }

    /**
     * 删除持久化节点
     *
     * @param path path
     * @return boolean
     */
    public boolean deleteNode(String path) {
        try {
            // version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zooKeeper.delete(path, -1);
            return true;
        } catch (Exception e) {
            log.error("【删除持久化节点异常】{},{}", path, e);
            return false;
        }
    }

    /**
     * 获取当前节点的子节点(不包含孙子节点)
     *
     * @param path 父节点path
     */
    public List<String> getChildren(String path) {
        try {
            return zooKeeper.getChildren(path, false);
        } catch (Exception e) {
            log.error("【获取持久化节点异常】{},{}", path, e);
            return Collections.emptyList();
        }
    }

    /**
     * 获取指定节点的值
     *
     * @param path    path
     * @param watcher watcher
     * @return string
     */
    public String getData(String path, Watcher watcher) {
        try {
            var stat = new Stat();
            var bytes = zooKeeper.getData(path, watcher, stat);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
