package com.livk.autoconfigure.oss.support.minio;

import com.livk.autoconfigure.oss.OSSProperties;
import com.livk.autoconfigure.oss.support.AbstractService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Minio service.
 *
 * @author livk
 */
public class MinioService extends AbstractService<MinioClient> {

    private static final String POLICY_JSON = "{" +
                                              "  \"Statement\": [" +
                                              "    {" +
                                              "      \"Action\": \"s3:GetObject\"," +
                                              "      \"Effect\": \"Allow\"," +
                                              "      \"Principal\": \"*\"," +
                                              "      \"Resource\": [" +
                                              "        \"arn:aws:s3:::${bucketName}/*\"" +
                                              "      ]" +
                                              "    }" +
                                              "  ]," +
                                              "  \"Version\": \"2012-10-17\"" +
                                              "}";

    /**
     * Instantiates a new Minio service.
     *
     * @param properties the properties
     */
    public MinioService(OSSProperties properties) {
        super(properties);
    }

    @Override
    protected MinioClient instance(OSSProperties properties) {
        return new MinioClient.Builder()
                .endpoint(properties.endpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

    @SneakyThrows
    @Override
    public void createBucket(String bucketName) {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            //设置桶策略
            String config = POLICY_JSON.replaceAll("\\$\\{bucketName}", bucketName);
            client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(config)
                    .build());
        }
    }

    @SneakyThrows
    @Override
    public List<String> allBuckets() {
        return client.listBuckets()
                .stream()
                .map(Bucket::name)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void removeBucket(String bucketName) {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    @Override
    public boolean exist(String bucketName, String fileName) {
        try {
            return client.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    @Override
    public void upload(String bucketName, String fileName, InputStream inputStream) {
        client.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(inputStream, inputStream.available(), -1)
                .build());
    }

    @SneakyThrows
    @Override
    public InputStream download(String bucketName, String fileName) {
        return client.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    @SneakyThrows
    @Override
    public void removeBucket(String bucketName, String fileName) {
        client.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    @SneakyThrows
    @Override
    public void removeObjs(String bucketName) {
        List<String> objs = getAllObj(bucketName);
        for (String obj : objs) {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(obj)
                    .build());
        }
    }

    @SneakyThrows
    @Override
    public String getStrUrl(String bucketName, String fileName) {
        return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .method(Method.GET)
                .build());
    }

    @Override
    public void close() {
        client = null;
    }

    @SneakyThrows
    @Override
    public List<String> getAllObj(String bucketName) {
        Iterable<Result<Item>> results = client.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .build());
        List<String> list = new ArrayList<>();
        for (Result<Item> result : results) {
            list.add(result.get().objectName());
        }
        return list;
    }
}
