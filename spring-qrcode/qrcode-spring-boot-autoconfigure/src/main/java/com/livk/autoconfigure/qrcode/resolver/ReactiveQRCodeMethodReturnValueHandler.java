package com.livk.autoconfigure.qrcode.resolver;


import com.livk.autoconfigure.qrcode.annotation.QRCode;
import com.livk.autoconfigure.qrcode.exception.QRCodeException;
import com.livk.autoconfigure.qrcode.util.QRCodeUtils;
import com.livk.util.AnnotationUtils;
import com.livk.util.JacksonUtils;
import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
 * @date 2022/10/29
 */
public class ReactiveQRCodeMethodReturnValueHandler implements HandlerResultHandler, Ordered {

    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    @Override
    public boolean supports(HandlerResult result) {
        return AnnotationUtils.hasAnnotation(result, QRCode.class);
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        QRCode qrCode = AnnotationUtils.getAnnotation(result, QRCode.class);
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
        String text = JacksonUtils.toJsonStr(value);
        byte[] bytes = QRCodeUtils.getQRCodeImage(text, qrCode.width(), qrCode.height(), qrCode.type());
        Resource resource = new ByteArrayResource(bytes);
        Flux<DataBuffer> bufferFlux = DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
        return response.writeWith(bufferFlux);
    }

    @Override
    public int getOrder() {
        //提高优先级，不然会被@ResponseBody优先处理掉
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
