package com.livk.redisson.bloom.support;


import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p>
 * RedissonBloom
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@Component
@RequiredArgsConstructor
public class RedissonBloom {

    private final RedissonClient redissonClient;
    private RBloomFilter<String> filter;

    @PostConstruct
    public void init() {
        this.filter = redissonClient.getBloomFilter("redisson:bloom:filter");
        this.filter.tryInit(Integer.MAX_VALUE / 10, 0.003);
    }

    public void addKey(String data) {
        filter.add(data);
    }

    public boolean hasKey(String data) {
        return filter.contains(data);
    }
}
