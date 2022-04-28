package com.livk.redisson.bloom.support;

import com.livk.common.redis.supprot.LivkRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * <p>
 * RedisBloom
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@Component
public class RedisBloom {

    // 布隆过滤器key1
    private static final String BLOOM_KEY = "redis:bloom:filter";
    // 初始化集合长度
    private static final int length = Integer.MAX_VALUE;
    // 准备hash计算次数
    /**
     * 准备自定义哈希算法需要用到的质数，因为一条数据需要hash计算5次 且5次的结果要不一样
     */
    private static final int[] primeNums = new int[]{17, 19, 29, 31, 37};

    private final ValueOperations<String, Object> forValue;

    public RedisBloom(LivkRedisTemplate livkRedisTemplate) {
        this.forValue = livkRedisTemplate.opsForValue();
    }

    public void addKey(String data) {
        for (var primeNum : primeNums) {
            var hashcode = this.hash(data, primeNum);
            var bitIndex = hashcode & (length - 1);
            forValue.setBit(BLOOM_KEY, bitIndex, true);
        }
    }

    public boolean hasKey(String data) {
        for (var primeNum : primeNums) {
            var hashcode = this.hash(data, primeNum);
            var bitIndex = hashcode & (length - 1);
            if (Boolean.FALSE.equals(forValue.getBit(BLOOM_KEY, bitIndex))) {
                return false;
            }
        }
        return true;
    }

    public int hash(String data, int prime) {
        var h = 0;
        for (var c : data.toCharArray()) {
            h = prime * h + c;
        }
        return h;
    }
}
