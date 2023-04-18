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

package com.livk.autoconfigure.oss.support;

import java.io.InputStream;
import java.util.List;

/**
 * The interface Oss operations.
 *
 * @author livk
 */
public sealed interface OSSOperations extends AutoCloseable permits AbstractService, OSSTemplate {

    /**
     * Exist boolean.
     *
     * @param bucketName the bucket name
     * @return the boolean
     */
    boolean exist(String bucketName);

    /**
     * Create bucket.
     *
     * @param bucketName the bucket name
     */
    void createBucket(String bucketName);

    /**
     * All buckets list.
     *
     * @return the list
     */
    List<String> allBuckets();

    /**
     * Remove.
     *
     * @param bucketName the bucket name
     */
    void removeBucket(String bucketName);

    /**
     * Exist boolean.
     *
     * @param bucketName the bucket name
     * @param fileName   the file name
     * @return the boolean
     */
    boolean exist(String bucketName, String fileName);

    /**
     * Upload.
     *
     * @param bucketName  the bucket name
     * @param fileName    the file name
     * @param inputStream the input stream
     */
    void upload(String bucketName, String fileName, InputStream inputStream);

    /**
     * Download input stream.
     *
     * @param bucketName the bucket name
     * @param fileName   the file name
     * @return the input stream
     */
    InputStream download(String bucketName, String fileName);

    /**
     * Remove.
     *
     * @param bucketName the bucket name
     * @param fileName   the file name
     */
    void removeBucket(String bucketName, String fileName);

    /**
     * Remove objs.
     *
     * @param bucketName the bucket name
     */
    void removeObjs(String bucketName);

    /**
     * Gets str url.
     *
     * @param bucketName the bucket name
     * @param fileName   the file name
     * @return the str url
     */
    String getStrUrl(String bucketName, String fileName);

    /**
     * Gets all obj.
     *
     * @param bucketName the bucket name
     * @return the all obj
     */
    List<String> getAllObj(String bucketName);
}
