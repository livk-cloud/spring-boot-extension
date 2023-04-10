package com.livk.autoconfigure.limit.support;

import java.util.concurrent.TimeUnit;

/**
 * The interface Limit executor.
 *
 * @author livk
 */
public interface LimitExecutor {

    /**
     * 在给定的时间段里最多的访问限制次数(超出次数返回false)；等下个时间段开始，才允许再次被访问(返回true)，周而复始
     *
     * @param compositeKey     资源Key
     * @param rate             最多的访问限制次数
     * @param rateInterval     给定的时间段(单位秒)
     * @param rateIntervalUnit the rate interval unit
     * @return boolean boolean
     */
    boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit);
}
