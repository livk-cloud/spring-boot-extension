package com.livk.testcontainers.spring;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * @author livk
 */
public interface ZookeeperConnectionDetails extends ConnectionDetails {

	default String getConnectString() {
		return null;
	}

}
