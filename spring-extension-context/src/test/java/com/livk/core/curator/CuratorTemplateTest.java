package com.livk.core.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * CuratorTemplateTest
 * </p>
 *
 * @author livk
 */
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CuratorTemplateTest {

	CuratorTemplate template;

	@BeforeEach
	public void init() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(50, 10, 500);
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
			.retryPolicy(retryPolicy)
			.connectString("livk.com:2181")
			.build();
		curatorFramework.start();
		template = new CuratorTemplate(curatorFramework);
	}

	@AfterEach
	public void after() throws IOException {
		template.close();
	}

	@Test
	void setDataAsync() throws Exception {
		template.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertArrayEquals("data".getBytes(), template.getNode("/node"));
		template.setDataAsync("/node", "setData".getBytes(StandardCharsets.UTF_8));
		assertArrayEquals("setData".getBytes(), template.getNode("/node"));
		template.deleteNode("/node");
	}

	@Test
	void getLock() throws Exception {
		InterProcessLock lock = template.getLock("/node/lock");
		assertEquals(InterProcessMutex.class, lock.getClass());
		assertTrue(lock.acquire(3, TimeUnit.SECONDS));
		lock.release();
		template.deleteNode("/node");
	}

	@Test
	void getReadLock() throws Exception {
		InterProcessLock read = template.getReadLock("/node/lock");
		assertEquals(InterProcessReadWriteLock.ReadLock.class, read.getClass());
		assertTrue(read.acquire(3, TimeUnit.SECONDS));
		read.release();
		template.deleteNode("/node");
	}

	@Test
	void getWriteLock() throws Exception {
		InterProcessLock write = template.getWriteLock("/node/lock");
		assertEquals(InterProcessReadWriteLock.WriteLock.class, write.getClass());
		assertTrue(write.acquire(3, TimeUnit.SECONDS));
		write.release();

		template.deleteNode("/node");
	}

}
