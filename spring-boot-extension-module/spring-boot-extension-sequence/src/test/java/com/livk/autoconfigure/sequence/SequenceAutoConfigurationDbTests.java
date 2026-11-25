/*
 * Copyright 2021-present the original author or authors.
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
 */

package com.livk.autoconfigure.sequence;

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceProvider;
import com.livk.context.sequence.interceptor.SequenceInterceptor;
import com.livk.context.sequence.support.RangeSequenceProvider;
import com.livk.testcontainers.DockerImageNames;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class SequenceAutoConfigurationDbTests {

	private static final int THREADS = 12;

	private static final int PER_THREAD = 40;

	@Container
	static final PostgreSQLContainer postgresql = new PostgreSQLContainer(DockerImageNames.postgres())
		.withExposedPorts(PostgreSQLContainer.POSTGRESQL_PORT)
		.withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("sequence");

	@Test
	void configuresDatabaseBackendAndIssuesUniqueIdsConcurrently() {
		new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(SequenceAutoConfiguration.class))
			.withUserConfiguration(DbConfig.class)
			.withPropertyValues("spring.sequence.type=db", "spring.sequence.step=10",
					"spring.sequence.biz-name=autoconfig-db", "spring.datasource.username=" + postgresql.getUsername(),
					"spring.datasource.password=" + postgresql.getPassword(),
					"spring.datasource.url=jdbc:postgresql://" + postgresql.getHost() + ":"
							+ postgresql.getFirstMappedPort() + "/" + postgresql.getDatabaseName())
			.run((context) -> {
				assertThat(context).hasSingleBean(SequenceInterceptor.class);
				assertThat(context).hasSingleBean(RangeSequenceProvider.class);
				assertThat(context).hasSingleBean(Sequence.class);

				SequenceProvider provider = context.getBean(SequenceProvider.class);
				Sequence sequence = provider.get("autoconfig-db-concurrent");

				Set<Long> ids = ConcurrentHashMap.newKeySet();
				AtomicReference<Throwable> error = new AtomicReference<>();
				CountDownLatch ready = new CountDownLatch(THREADS);
				CountDownLatch start = new CountDownLatch(1);
				CountDownLatch done = new CountDownLatch(THREADS);
				try (ExecutorService executor = Executors.newFixedThreadPool(THREADS)) {
					for (int i = 0; i < THREADS; i++) {
						executor.submit(() -> {
							ready.countDown();
							try {
								start.await();
								for (int j = 0; j < PER_THREAD; j++) {
									assertThat(ids.add(sequence.nextValue())).isTrue();
								}
							}
							catch (Throwable ex) {
								error.compareAndSet(null, ex);
							}
							finally {
								done.countDown();
							}
						});
					}
					assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
					start.countDown();
					assertThat(done.await(60, TimeUnit.SECONDS)).isTrue();
				}

				assertThat(error.get()).isNull();
				assertThat(ids).hasSize(THREADS * PER_THREAD);
				assertThat(context.getBean(Sequence.class).nextValue()).isPositive();
			});
	}

	@EnableSequence
	@TestConfiguration(proxyBeanMethods = false)
	static class DbConfig {

		@Bean(destroyMethod = "close")
		DataSource dataSource(@Value("${spring.datasource.url}") String url,
				@Value("${spring.datasource.username}") String username,
				@Value("${spring.datasource.password}") String password) {
			HikariDataSource dataSource = new HikariDataSource();
			dataSource.setDriverClassName(Driver.class.getName());
			dataSource.setJdbcUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
		}

	}

}
