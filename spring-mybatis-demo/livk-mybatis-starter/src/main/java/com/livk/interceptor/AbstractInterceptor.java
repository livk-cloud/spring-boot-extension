package com.livk.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;

/**
 * <p>
 * AbstractInterceptor
 * </p>
 *
 * @author livk
 * @date 2022/3/25
 */
public interface AbstractInterceptor extends Interceptor {
    @Override
    default Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
