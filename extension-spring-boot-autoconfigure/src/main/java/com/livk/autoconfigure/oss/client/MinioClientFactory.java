package com.livk.autoconfigure.oss.client;

import com.livk.auto.service.annotation.SpringFactories;
import io.minio.MinioClient;

/**
 * The type Minio client factory.
 *
 * @author livk
 */
@SpringFactories(OSSClientFactory.class)
public class MinioClientFactory implements OSSClientFactory<MinioClient> {

    @Override
    public MinioClient instance(String endpoint, String accessKey, String secretKey) {
        return new MinioClient.Builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public String prefix() {
        return "minio";
    }
}
