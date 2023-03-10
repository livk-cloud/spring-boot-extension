package com.livk.redisson.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * ScheduleHandlerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
class ScheduleHandlerTest {

    @Autowired
    ScheduleHandler scheduleHandler;

    @Test
    void start() {
        scheduleHandler.start();
    }
}
