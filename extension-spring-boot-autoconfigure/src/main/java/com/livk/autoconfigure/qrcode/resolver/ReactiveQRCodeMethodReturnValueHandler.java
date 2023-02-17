package com.livk.autoconfigure.qrcode.resolver;


import com.livk.autoconfigure.qrcode.annotation.QRCode;
import com.livk.autoconfigure.qrcode.exception.QRCodeException;
import com.livk.autoconfigure.qrcode.util.QRCodeUtils;
import com.livk.commons.io.DataBufferUtils;
import com.livk.commons.jackson.JacksonUtils;
import com.livk.commons.util.AnnotationUtils;
import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * ReactiveQRCodeMethodReturnValueHandler
 * </p>
 *
 * @author livk
 */
public class ReactiveQRCodeMethodReturnValueHandler implements HandlerResultHandler, Ordered {

    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    @Override
    public boolean supports(@NonNull HandlerResult result) {
        return AnnotationUtils.hasAnnotationElement(result.getReturnTypeSource(), QRCode.class);
    }

    @NonNull
    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        QRCode qrCode = AnnotationUtils.getAnnotationElement(result.getReturnTypeSource(), QRCode.class);
        ServerHttpResponse response = exchange.getResponse();
        ResolvableType returnType = result.getReturnType();
        ReactiveAdapter adapter = adapterRegistry.getAdapter(returnType.resolve(), returnValue);
        if (adapter != null) {
            if (Mono.class.isAssignableFrom(returnType.toClass())) {
                Mono<?> mono = (Mono<?>) returnValue;
                Assert.notNull(mono, "mono not be null");
                return mono.flatMap(o -> write(o, qrCode, response));
            }
        } else {
            return write(returnValue, qrCode, response);
        }
        throw new QRCodeException("current type is not supported:" + returnType.toClass());
    }

    private Mono<Void> write(Object value, QRCode qrCode, ServerHttpResponse response) {
        String text = JacksonUtils.writeValueAsString(value);
        byte[] bytes = QRCodeUtils.getQRCodeImage(text, qrCode.width(), qrCode.height(), qrCode.type());
        Flux<DataBuffer> bufferFlux = DataBufferUtils.transform(bytes);
        return response.writeWith(bufferFlux);
    }

    @Override
    public int getOrder() {
        //提高优先级，不然会被@ResponseBody优先处理掉
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
