package com.livk.commons.endpoint;

import com.livk.auto.service.annotation.SpringAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * SystemController
 * </p>
 *
 * @author livk
 */
@RestController
@SpringAutoService
@RequiredArgsConstructor
public class SystemController {

    private final ApplicationContext applicationContext;

    /**
     * Shutdown.
     */
    @PostMapping("shutdown")
    public void shutdown() {
        System.exit(SpringApplication.exit(applicationContext));
    }

}
