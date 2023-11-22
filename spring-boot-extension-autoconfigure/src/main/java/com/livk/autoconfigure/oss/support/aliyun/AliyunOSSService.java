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

package com.livk.autoconfigure.oss.support.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.SetBucketCORSRequest;
import com.livk.autoconfigure.oss.support.AbstractService;
import com.livk.commons.util.DateUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Aliyun oss service.
 *
 * @author livk
 */
public class AliyunOSSService extends AbstractService<OSS> {

	@Override
	public boolean exist(String bucketName) {
		return client.doesBucketExist(bucketName);
	}

	@Override
	public void createBucket(String bucketName) {
		if (!client.doesBucketExist(bucketName)) {
			client.createBucket((bucketName));
			// 设置cors
			SetBucketCORSRequest request = new SetBucketCORSRequest(bucketName);
			SetBucketCORSRequest.CORSRule rule = new SetBucketCORSRequest.CORSRule();
			rule.addAllowedMethod("GET");
			rule.addAllowedMethod("POST");
			rule.addAllowedMethod("PUT");
			rule.addAllowedMethod("DELETE");
			rule.addAllowdOrigin("*");
			request.addCorsRule(rule);
			client.setBucketCORS(request);
			client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
		}
	}

	@Override
	public List<String> allBuckets() {
		return client.listBuckets().stream().map(Bucket::getName).toList();
	}

	@Override
	public void removeObj(String bucketName) {
		client.deleteBucket(bucketName);
	}

	@Override
	public boolean exist(String bucketName, String fileName) {
		client.doesObjectExist(bucketName, fileName);
		return false;
	}

	@Override
	public void upload(String bucketName, String fileName, InputStream inputStream) {
		client.putObject(bucketName, fileName, inputStream);
	}

	@Override
	public InputStream download(String bucketName, String fileName) {
		return client.getObject(bucketName, fileName).getObjectContent();
	}

	@Override
	public void removeObj(String bucketName, String fileName) {
		client.deleteObject(bucketName, fileName);
	}

	@Override
	public String getStrUrl(String bucketName, String fileName) {
		return getStrUrl(bucketName, fileName, 7);
	}

	@Override
	public String getStrUrl(String bucketName, String fileName, int expires) {
		LocalDateTime time = LocalDateTime.now().plusDays(expires);
		return client.generatePresignedUrl(bucketName, fileName, DateUtils.date(time)).toString();
	}

	@Override
	public List<String> getAllObj(String bucketName) {
		return client.listObjects(bucketName).getObjectSummaries().stream().map(OSSObjectSummary::getKey).toList();
	}

	@Override
	public void close() throws Exception {
		client.shutdown();
	}

}
