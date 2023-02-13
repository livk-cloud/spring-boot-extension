package com.livk.autoconfigure.oss.client;

import io.minio.MinioClient;

/**
 * @author livk
 */
public class MinioClientFactory implements OSSClientFactory<MinioClient> {

    @Override
    public MinioClient instance(String endpoint, String accessKey, String secretKey) {
        return new MinioClient.Builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
