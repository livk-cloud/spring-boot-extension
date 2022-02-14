package com.livk.socket.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * ServerConfigurator
 * </p>
 *
 * @author livk
 * @date 2022/2/14
 */
@Slf4j
public class ServerConfigurator extends ServerEndpointConfig.Configurator {

    public final static String AUTHORIZATION = "Authorization";

    @Override
    public boolean checkOrigin(String originHeaderValue) {
        var servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(servletRequestAttributes, "servletRequestAttributes not be null!");
        var request = servletRequestAttributes.getRequest();
        var token = request.getHeader(AUTHORIZATION);
        if ("livk123".equals(token)) {
            return super.checkOrigin(originHeaderValue);
        } else {
            log.info("缺少参数!");
            return false;
        }
    }
}
