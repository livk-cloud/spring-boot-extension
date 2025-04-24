package com.livk.testcontainers.spring;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * @author livk
 */
public interface MinIOConnectionDetails extends ConnectionDetails {

	default String getEndpoint() {
		return null;
	}

	default String getUserName() {
		return null;
	}

	default String getPassword() {
		return null;
	}

}
