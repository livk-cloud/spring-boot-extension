package com.livk.context.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;

/**
 * @author livk
 */
public interface LockProcess {

	InterProcessLock getLock(CuratorFramework framework, String path);

}
