package com.livk.autoconfigure.useragent.yauaa.support;

import lombok.experimental.UtilityClass;
import nl.basjes.parse.useragent.UserAgent;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * <p>
 * ReactiveUserAgentContext
 * </p>
 *
 * @author livk
 * @date 2022/10/11
 */
@UtilityClass
public final class ReactiveUserAgentContextHolder {

    private static final Class<?> USER_AGENT_KEY = UserAgent.class;

    public Mono<UserAgent> get() {
        return Mono.deferContextual(Mono::just)
                .cast(Context.class)
                .filter(ReactiveUserAgentContextHolder::hasContext)
                .flatMap(ReactiveUserAgentContextHolder::getContext);
    }

    private static boolean hasContext(Context context) {
        return context.hasKey(USER_AGENT_KEY);
    }

    private static Mono<UserAgent> getContext(Context context) {
        return context.<Mono<UserAgent>>get(USER_AGENT_KEY);
    }

    public static Function<Context, Context> clearContext() {
        return (context) -> context.delete(USER_AGENT_KEY);
    }

    public static Context withContext(Mono<? extends UserAgent> capabilities) {
        return Context.of(USER_AGENT_KEY, capabilities);
    }

    public static Context withContext(UserAgent capabilities) {
        return withContext(Mono.just(capabilities));
    }
}
