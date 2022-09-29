package com.livk.zookeeper.service.impl;

import com.livk.zookeeper.service.CuratorService;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * CuratorServiceImpl
 * </p>
 *
 * @author livk
 * @date 2022/8/23
 */
@Service
@RequiredArgsConstructor
public class CuratorServiceImpl implements CuratorService {

    private final CuratorFramework curatorClient;

    @Override
    public String createNode(String path, String data) throws Exception {
        return curatorClient.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
    }

    @Override
    public String createTypeNode(CreateMode nodeType, String path, String data) throws Exception {
        return curatorClient.create().creatingParentsIfNeeded().withMode(nodeType).forPath(path, data.getBytes());
    }

    @Override
    public String createTypeSeqNode(CreateMode nodeType, String path, String data) throws Exception {
        return curatorClient.create().creatingParentsIfNeeded().withProtection().withMode(nodeType).forPath(path, data.getBytes());
    }

    @Override
    public Stat setData(String path, String data) throws Exception {
        return curatorClient.setData().forPath(path, data.getBytes());
    }

    @Override
    public Stat setDataAsync(String path, String data) throws Exception {
        CuratorListener listener = (client, event) -> {
            //examine event for details
        };
        curatorClient.getCuratorListenable().addListener(listener);
        return curatorClient.setData().inBackground().forPath(path, data.getBytes());
    }

    @Override
    public void deleteNode(String path) throws Exception {
        curatorClient.delete().deletingChildrenIfNeeded().forPath(path);
    }

    @Override
    public List<String> watchedGetChildren(String path) throws Exception {
        return curatorClient.getChildren().watched().forPath(path);
    }

    @Override
    public List<String> watchedGetChildren(String path, Watcher watcher) throws Exception {
        return curatorClient.getChildren().usingWatcher(watcher).forPath(path);
    }

    @Override
    public void getLock(String path, int waitTime) {
        InterProcessLock lock = new InterProcessMutex(curatorClient, path);
        try {
            if (lock.acquire(waitTime, TimeUnit.MILLISECONDS)) {
                System.out.println("to do something");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
