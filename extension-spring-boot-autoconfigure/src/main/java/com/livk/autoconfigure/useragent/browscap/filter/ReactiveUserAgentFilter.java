package com.livk.autoconfigure.useragent.browscap.filter;

import com.livk.autoconfigure.useragent.browscap.support.ReactiveUserAgentContextHolder;
import com.livk.autoconfigure.useragent.browscap.util.UserAgentUtils;
import com.livk.autoconfigure.useragent.filter.AbstractReactiveUserAgentFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * <p>
 * ReactiveUserAgentFilter
 * </p>
 *
 * @author livk
 */
public class ReactiveUserAgentFilter extends AbstractReactiveUserAgentFilter {

    @Override
    protected Context write(ServerWebExchange exchange) {
        return ReactiveUserAgentContextHolder.withContext(UserAgentUtils.parse(exchange.getRequest()));
    }

    @Override
    protected Function<Context, Context> clear() {
        return ReactiveUserAgentContextHolder.clearContext();
    }
}
