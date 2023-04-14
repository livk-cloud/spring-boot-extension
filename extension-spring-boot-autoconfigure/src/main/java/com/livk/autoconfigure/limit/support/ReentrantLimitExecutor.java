package com.livk.autoconfigure.limit.support;

import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
public abstract class ReentrantLimitExecutor implements LimitExecutor {

    public static final String ATTRIBUTE_NAME = "limit";

    /**
     * 同一个request仅只被限流一次
     * 在给定的时间段里最多的访问限制次数(超出次数返回false)；等下个时间段开始，才允许再次被访问(返回true)，周而复始
     *
     * @param compositeKey     资源Key
     * @param rate             最多的访问限制次数
     * @param rateInterval     给定的时间段(单位秒)
     * @param rateIntervalUnit the rate interval unit
     * @return boolean
     */
    protected abstract boolean reentrantTryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit);

    @Override
    public final boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        HttpServletRequest request = WebUtils.request();
        Object limitAttribute = request.getAttribute(ATTRIBUTE_NAME);
        if (limitAttribute instanceof Boolean bool && bool) {
            return true;
        } else {
            boolean bool = reentrantTryAccess(compositeKey, rate, rateInterval, rateIntervalUnit);
            request.setAttribute(ATTRIBUTE_NAME, bool);
            return bool;
        }
    }
}
