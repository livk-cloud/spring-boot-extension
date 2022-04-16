package com.livk.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * ResponseUtil
 * </p>
 *
 * @author livk
 * @date 2021/11/2
 */
@UtilityClass
public class ResponseUtils {

    public HttpServletResponse getResponse() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        var servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        Assert.notNull(servletRequestAttributes, "attributes not null!");
        return servletRequestAttributes.getResponse();
    }

    public void out(Object data) {
        var response = ResponseUtils.getResponse();
        Assert.notNull(response, "response not null!");
        ResponseUtils.out(response, JacksonUtils.toJsonStr(data));
    }

    public void out(HttpServletResponse response,Object data) {
        ResponseUtils.out(response, JacksonUtils.toJsonStr(data));
    }

    /**
     * 根据response写入返回值
     *
     * @param response response
     * @param message  写入的信息
     */
    public void out(HttpServletResponse response, String message) {
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(message);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
