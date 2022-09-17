package com.livk.spring;

import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * <p>
 * CustomizationBean
 * </p>
 *
 * @author livk
 * @date 2022/9/17
 */
@Component
@ConditionalOnClass(Undertow.class)
public class UndertowCustomizationBean implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo
                .addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo",
                        new WebSocketDeploymentInfo().setBuffers(new DefaultByteBufferPool(false, 1024))));
    }
}
