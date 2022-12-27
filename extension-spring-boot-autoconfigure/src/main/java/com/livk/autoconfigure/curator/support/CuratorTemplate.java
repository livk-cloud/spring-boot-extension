package com.livk.autoconfigure.curator.support;

import com.livk.autoconfigure.curator.lock.ZkLockType;
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
 * @date 2022/12/27
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

    public Stat setDataAsync(String path, String data) throws Exception {
        return setDataAsync(path, data, (client, event) -> {

        });
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

    public InterProcessLock getLock(String path) {
        return getLock(path, ZkLockType.LOCK);
    }

    public InterProcessLock getReadLock(String path) {
        return getLock(path, ZkLockType.READ);
    }

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
