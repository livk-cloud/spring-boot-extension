package com.livk.autoconfigure.qrcode.resolver;

import com.livk.autoconfigure.qrcode.annotation.QRCode;
import com.livk.autoconfigure.qrcode.util.QRCodeUtils;
import com.livk.commons.jackson.JacksonUtils;
import com.livk.commons.util.AnnotationUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;

/**
 * <p>
 * QRCodeMethodReturnValueHandler
 * </p>
 *
 * @author livk
 *
 */
public class QRCodeMethodReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(@NonNull MethodParameter returnType) {
        return AnnotationUtils.hasAnnotationElement(returnType, QRCode.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, @NonNull MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws IOException {
        mavContainer.setRequestHandled(true);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        QRCode qrCode = AnnotationUtils.getAnnotationElement(returnType, QRCode.class);
        Assert.notNull(response, "response not be null");
        Assert.notNull(qrCode, "excelReturn not be null");
        String text = JacksonUtils.toJsonStr(returnValue);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            QRCodeUtils.getQRCodeImage(text, qrCode.width(), qrCode.height(), outputStream, qrCode.type());
        }
    }

    @Override
    public boolean isAsyncReturnValue(Object returnValue, @NonNull MethodParameter returnType) {
        return AnnotationUtils.hasAnnotationElement(returnType, QRCode.class);
    }

}
