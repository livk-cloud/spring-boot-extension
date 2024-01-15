package com.livk.core.curator;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
@ContextConfiguration(classes = CuratorConfig.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CuratorTemplateTest {

	@Autowired
	CuratorTemplate template;

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
