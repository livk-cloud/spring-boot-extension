package com.livk.starter01;

import com.livk.auto.service.annotation.SpringAutoService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * AnnoTest
 * </p>
 *
 * @author livk
 */
@Slf4j
@LivkComponent
@SpringAutoService
public class AnnoTest {

    public void show() {
        log.info("1");
    }

}
