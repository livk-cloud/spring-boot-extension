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
