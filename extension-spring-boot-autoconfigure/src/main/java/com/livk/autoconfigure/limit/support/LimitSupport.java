package com.livk.autoconfigure.limit.support;

import com.livk.autoconfigure.limit.annotation.Limit;
import com.livk.commons.spring.spel.SpringExpressionResolver;
import com.livk.commons.web.util.WebUtils;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * The type Limit support.
 *
 * @author livk
 */
@RequiredArgsConstructor(staticName = "of")
public class LimitSupport {

    /**
     * SpEL表达式解析器
     */
    private final SpringExpressionResolver resolver = new SpringExpressionResolver();


    private final LimitExecutor limitExecutor;

    /**
     * Exec boolean.
     *
     * @param limit  the limit
     * @param method the method
     * @param args   the args
     * @return the boolean
     */
    public boolean exec(Limit limit, Method method, Object[] args) {
        String key = limit.key();
        int rate = limit.rate();
        int rateInterval = limit.rateInterval();
        TimeUnit rateIntervalUnit = limit.rateIntervalUnit();
        String spELKey = resolver.evaluate(key, method, args);
        if (limit.restrictIp()) {
            String ip = WebUtils.realIp(WebUtils.request());
            spELKey = spELKey + "#" + ip;
        }
        return limitExecutor.tryAccess(spELKey, rate, rateInterval, rateIntervalUnit);
    }
}
