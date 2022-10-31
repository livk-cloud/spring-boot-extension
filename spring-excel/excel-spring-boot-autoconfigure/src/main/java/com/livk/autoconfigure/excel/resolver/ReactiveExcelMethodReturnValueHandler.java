package com.livk.autoconfigure.excel.resolver;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.autoconfigure.excel.exception.ExcelExportException;
import org.springframework.core.*;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ReactiveExcelMethodReturnValueHandler
 * </p>
 * <p>
 * {@link reactor.core.publisher.Flux} 和 {@link Mono}未适配
 * </p>
 *
 * @author livk
 * @date 2022/10/29
 */
public class ReactiveExcelMethodReturnValueHandler implements HandlerResultHandler, Ordered {
    public static final MediaType EXCEL_MEDIA_TYPE = new MediaType("application", "vnd.ms-excel");

    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    @Override
    public boolean supports(HandlerResult result) {
        return this.hasAnnotation(result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        if (returnValue == null) {
            return Mono.empty();
        }
        ExcelReturn excelReturn = this.getAnnotation(result);
        ServerHttpResponse response = exchange.getResponse();
        ResolvableType returnType = result.getReturnType();
        ReactiveAdapter adapter = adapterRegistry.getAdapter(returnType.resolve(), returnValue);
        if (adapter != null) {
            ResolvableType genericType = returnType.getGeneric();
//            ResolvableType elementType = getElementType(adapter, genericType);
            if (Collection.class.isAssignableFrom(genericType.toClass())) {
                Class<?> excelModelClass = genericType.resolveGeneric(0);
                Mono<Map<String, Collection<?>>> mono = ((Mono<Collection<?>>) returnValue).map(c -> Map.of("sheet", c));
                return this.write(excelReturn, response, excelModelClass, mono);
            } else if (Map.class.isAssignableFrom(genericType.toClass())) {
                Class<?> excelModelClass = genericType.getGeneric(1).resolveGeneric(0);
                Mono<Map<String, Collection<?>>> mono = (Mono<Map<String, Collection<?>>>) returnValue;
                return this.write(excelReturn, response, excelModelClass, mono);
            } else {
                throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
            }
        } else {
            if (Collection.class.isAssignableFrom(returnType.toClass())) {
                Class<?> excelModelClass = returnType.resolveGeneric(0);
                return this.write(excelReturn, response, excelModelClass, Map.of("sheet", (Collection<?>) returnValue));
            } else if (Map.class.isAssignableFrom(returnType.toClass())) {
                Map<String, Collection<?>> map = (Map<String, Collection<?>>) returnValue;
                Class<?> excelModelClass = returnType.getGeneric(1).resolveGeneric(0);
                return this.write(excelReturn, response, excelModelClass, map);
            } else {
                throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
            }
        }
    }

    public Mono<Void> write(ExcelReturn excelReturn, ServerHttpResponse response, Class<?> excelModelClass, Mono<Map<String, Collection<?>>> result) {
        return result.flatMap(r -> this.write(excelReturn, response, excelModelClass, r));
    }


    public Mono<Void> write(ExcelReturn excelReturn, ServerHttpResponse response, Class<?> excelModelClass, Map<String, Collection<?>> result) {
        this.setResponse(excelReturn, response);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ExcelWriter writer = EasyExcel.write(outputStream, excelModelClass).build()) {
            for (Map.Entry<String, Collection<?>> entry : result.entrySet()) {
                WriteSheet sheet = EasyExcel.writerSheet(entry.getKey()).build();
                writer.write(entry.getValue(), sheet);
            }
        }
        Resource resource = new ByteArrayResource(outputStream.toByteArray());
        Flux<DataBuffer> bufferFlux = DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
        return response.writeWith(bufferFlux);
    }

    private void setResponse(ExcelReturn excelReturn, ServerHttpResponse response) {
        String fileName = excelReturn.fileName().concat(excelReturn.suffix().getName());
        MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                .orElse(EXCEL_MEDIA_TYPE);
        HttpHeaders headers = response.getHeaders();
        headers.setContentType(mediaType);
        headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
        ContentDisposition contentDisposition = ContentDisposition.parse("attachment;filename=" + fileName);
        headers.setContentDisposition(contentDisposition);
    }

    private ResolvableType getElementType(ReactiveAdapter adapter, ResolvableType genericType) {
        if (adapter.isNoValue()) {
            return ResolvableType.forClass(Void.class);
        } else {
            return genericType != ResolvableType.NONE ? genericType : ResolvableType.forClass(Object.class);
        }
    }

    private ExcelReturn getAnnotation(HandlerResult result) {
        MethodParameter returnType = result.getReturnTypeSource();
        ExcelReturn excelReturn = returnType.getMethodAnnotation(ExcelReturn.class);
        if (excelReturn == null) {
            Class<?> containingClass = returnType.getContainingClass();
            excelReturn = AnnotatedElementUtils.getMergedAnnotation(containingClass, ExcelReturn.class);
        }
        return excelReturn;
    }

    private boolean hasAnnotation(HandlerResult result) {
        MethodParameter returnType = result.getReturnTypeSource();
        Class<?> containingClass = returnType.getContainingClass();
        return (AnnotatedElementUtils.hasAnnotation(containingClass, ExcelReturn.class) ||
                returnType.hasMethodAnnotation(ExcelReturn.class));
    }

    @Override
    public int getOrder() {
        //提高优先级，不然会被@ResponseBody优先处理掉
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
