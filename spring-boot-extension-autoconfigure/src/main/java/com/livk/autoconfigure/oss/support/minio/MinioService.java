/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.oss.support.minio;

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

	@SneakyThrows
	@Override
	public boolean exist(String bucketName) {
		return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
	}

	@SneakyThrows
	@Override
	public void createBucket(String bucketName) {
		if (!this.exist(bucketName)) {
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
	public void removeObj(String bucketName) {
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
	public void removeObj(String bucketName, String fileName) {
		client.removeObject(RemoveObjectArgs.builder()
			.bucket(bucketName)
			.object(fileName)
			.build());
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

	@SneakyThrows
	@Override
	public String getStrUrl(String bucketName, String fileName, int expires) {
		return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
			.bucket(bucketName)
			.object(fileName)
			.method(Method.GET)
			.expiry(expires)
			.build());
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

	@Override
	public void close() {
		client = null;
	}
}
