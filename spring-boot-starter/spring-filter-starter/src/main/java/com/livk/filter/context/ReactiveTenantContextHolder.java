package com.livk.filter.context;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * <p>
 * ReactiveTenantContextHolder
 * </p>
 *
 * @author livk
 */
public abstract class ReactiveTenantContextHolder {

    public final static String ATTRIBUTES = "tenant";

    private static final Class<?> TENANT_ID_KEY = String.class;

    private static boolean hasContext(Context context) {
        return context.hasKey(TENANT_ID_KEY);
    }

    private static Mono<String> getContext(Context context) {
        return context.<Mono<String>>get(TENANT_ID_KEY);
    }

    public static Function<Context, Context> clearContext() {
        return (context) -> context.delete(TENANT_ID_KEY);
    }

    public static Context withContext(Mono<? extends String> tenantId) {
        return Context.of(TENANT_ID_KEY, tenantId);
    }

    public static Context withContext(String tenantId) {
        return withContext(Mono.just(tenantId));
    }

    public Mono<String> get() {
        return Mono.deferContextual(Mono::just)
                .cast(Context.class)
                .filter(ReactiveTenantContextHolder::hasContext)
                .flatMap(ReactiveTenantContextHolder::getContext);
    }
}
