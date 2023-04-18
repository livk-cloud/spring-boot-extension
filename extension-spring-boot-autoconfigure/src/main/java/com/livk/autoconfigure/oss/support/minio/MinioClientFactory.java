package com.livk.autoconfigure.oss.support.minio;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.autoconfigure.oss.client.OSSClientFactory;
import io.minio.MinioClient;

/**
 * The type Minio client factory.
 *
 * @author livk
 */
@SpringFactories
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
