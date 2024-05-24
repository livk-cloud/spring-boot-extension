/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.testcontainers.containers;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href=
 * "https://github.com/testcontainers/testcontainers-java/pull/8416/files">testcontainers
 * use apache kafka</a>
 *
 * @author livk
 * @see org.testcontainers.kafka.KafkaContainer
 */
@Deprecated(since = "1.3.0", forRemoval = true)
public class KafkaContainer extends GenericContainer<KafkaContainer> {

	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("apache/kafka");

	private static final int KAFKA_PORT = 9092;

	private static final String DEFAULT_INTERNAL_TOPIC_RF = "1";

	private static final String STARTER_SCRIPT = "/testcontainers_start.sh";

	private static final String DEFAULT_CLUSTER_ID = "4L6g3nShT-eMCtK--X86sw";

	public KafkaContainer(String imageName) {
		this(DockerImageName.parse(imageName));
	}

	public KafkaContainer(DockerImageName dockerImageName) {
		super(dockerImageName);
		dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);

		withExposedPorts(KAFKA_PORT);
		withEnv("CLUSTER_ID", DEFAULT_CLUSTER_ID);

		withEnv("KAFKA_LISTENERS",
				"PLAINTEXT://0.0.0.0:" + KAFKA_PORT + ",BROKER://0.0.0.0:9093, CONTROLLER://0.0.0.0:9094");
		withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "BROKER:PLAINTEXT,PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT");
		withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "BROKER");
		withEnv("KAFKA_PROCESS_ROLES", "broker,controller");
		withEnv("KAFKA_CONTROLLER_LISTENER_NAMES", "CONTROLLER");

		withEnv("KAFKA_NODE_ID", "1");
		withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", DEFAULT_INTERNAL_TOPIC_RF);
		withEnv("KAFKA_OFFSETS_TOPIC_NUM_PARTITIONS", DEFAULT_INTERNAL_TOPIC_RF);
		withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", DEFAULT_INTERNAL_TOPIC_RF);
		withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", DEFAULT_INTERNAL_TOPIC_RF);
		withEnv("KAFKA_LOG_FLUSH_INTERVAL_MESSAGES", Long.MAX_VALUE + "");
		withEnv("KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS", "0");

		withCommand("sh", "-c", "while [ ! -f " + STARTER_SCRIPT + " ]; do sleep 0.1; done; " + STARTER_SCRIPT);
		waitingFor(Wait.forLogMessage(".*Transitioning from RECOVERY to RUNNING.*", 1));
	}

	@Override
	protected void configure() {
		String firstNetworkAlias = getNetworkAliases().stream().findFirst().orElse(null);
		String networkAlias = getNetwork() != null ? firstNetworkAlias : "localhost";
		String controllerQuorumVoters = String.format("%s@%s:9094", getEnvMap().get("KAFKA_NODE_ID"), networkAlias);
		withEnv("KAFKA_CONTROLLER_QUORUM_VOTERS", controllerQuorumVoters);
	}

	@Override
	protected void containerIsStarting(InspectContainerResponse containerInfo) {
		String brokerAdvertisedListener = String.format("BROKER://%s:%s", containerInfo.getConfig().getHostName(),
				"9093");
		List<String> advertisedListeners = new ArrayList<>();
		advertisedListeners.add("PLAINTEXT://" + getBootstrapServers());
		advertisedListeners.add(brokerAdvertisedListener);
		String kafkaAdvertisedListeners = String.join(",", advertisedListeners);
		String command = "#!/bin/bash\n";
		// exporting KAFKA_ADVERTISED_LISTENERS with the container hostname
		command += String.format("export KAFKA_ADVERTISED_LISTENERS=%s%n", kafkaAdvertisedListeners);

		command += "/etc/kafka/docker/run \n";
		copyFileToContainer(Transferable.of(command, 0777), STARTER_SCRIPT);
	}

	public String getBootstrapServers() {
		return String.format("%s:%s", getHost(), getMappedPort(KAFKA_PORT));
	}

}
