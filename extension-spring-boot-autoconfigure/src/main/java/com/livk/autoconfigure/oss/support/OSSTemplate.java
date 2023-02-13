package com.livk.autoconfigure.oss.support;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.List;

/**
 * The type Oss template.
 *
 * @author livk
 */
@RequiredArgsConstructor
public final class OSSTemplate implements OSSOperations {

    private final AbstractService<?> ossService;

    @Override
    public boolean exist(String bucketName) {
        return ossService.exist(bucketName);
    }

    @Override
    public void createBucket(String bucketName) {
        ossService.createBucket(bucketName);
    }

    @Override
    public List<String> allBuckets() {
        return ossService.allBuckets();
    }

    @Override
    public void removeBucket(String bucketName) {
        ossService.removeBucket(bucketName);
    }

    @Override
    public void removeObjs(String bucketName) {
        ossService.removeObjs(bucketName);
    }

    public void removeBucketAndObj(String bucketName) {
        if (this.exist(bucketName)) {
            this.removeObjs(bucketName);
            this.removeBucket(bucketName);
        }
    }

    @Override
    public boolean exist(String bucketName, String fileName) {
        return ossService.exist(bucketName, fileName);
    }

    @Override
    public void upload(String bucketName, String fileName, InputStream inputStream) {
        ossService.upload(bucketName, fileName, inputStream);
    }

    @Override
    public InputStream download(String bucketName, String fileName) {
        return ossService.download(bucketName, fileName);
    }

    @Override
    public void removeBucket(String bucketName, String fileName) {
        ossService.removeBucket(bucketName, fileName);
    }

    @Override
    public List<String> getAllObj(String bucketName) {
        return ossService.getAllObj(bucketName);
    }

    @Override
    public String getStrUrl(String bucketName, String fileName) {
        return ossService.getStrUrl(bucketName, fileName);
    }

    @Override
    public void close() throws Exception {
        ossService.close();
    }

    /**
     * Get external link string.
     *
     * @param bucketName the bucket name
     * @param fileName   the file name
     * @return the string
     */
    public String getExternalLink(String bucketName, String fileName) {
        String url = getStrUrl(bucketName, fileName);
        int index = url.indexOf('?');
        if (index == -1) {
            return url;
        } else {
            return url.substring(0, index);
        }
    }
}
