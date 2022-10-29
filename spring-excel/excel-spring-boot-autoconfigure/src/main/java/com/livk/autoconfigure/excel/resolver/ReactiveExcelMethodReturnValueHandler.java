package com.livk.autoconfigure.excel.resolver;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.autoconfigure.excel.exception.ExcelExportException;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
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

    @Override
    public boolean supports(HandlerResult result) {
        return this.hasAnnotation(result);
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        ExcelReturn excelReturn = this.getAnnotation(result);
        ServerHttpResponse response = exchange.getResponse();
        if (returnValue instanceof Collection) {
            Class<?> excelModelClass = result.getReturnType().resolveGeneric(0);
            return this.write(excelReturn, response, excelModelClass, Map.of("sheet", (Collection<?>) returnValue));
        } else if (returnValue instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Collection<?>> map = (Map<String, Collection<?>>) returnValue;
            Class<?> excelModelClass = result.getReturnType().getGeneric(1).resolveGeneric(0);
            return this.write(excelReturn, response, excelModelClass, map);
        } else {
            throw new ExcelExportException("the return class is not java.util.Collection or java.util.Map");
        }
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
