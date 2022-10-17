package com.livk.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * DateUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/9/13
 */
@Slf4j
class DateUtilsTest {

    @Test
    void toTimestampTest() {
        Long result = DateUtils.toTimestamp(LocalDateTime.now());
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void fromTimestampTest() {
        LocalDateTime result = DateUtils.fromTimestamp(1663063303L);
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void toDateTest() {
        Date result = DateUtils.toDate(LocalDate.now());
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void testToDateTest() {
        Date result = DateUtils.toDate(LocalDateTime.now());
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void toLocalDateTimeTest() {
        LocalDateTime result = DateUtils.toLocalDateTime(new Date());
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void formatTest() {
        String result = DateUtils.format(LocalDateTime.now(), DateTimeFormatter.ofPattern(DateUtils.YMD_HMS));
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void testFormatTest() {
        String result = DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS);
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void parseTest() {
        LocalDateTime result = DateUtils.parse("2022-09-13 18:05:12", DateUtils.YMD_HMS);
        LogUtils.info("result:{}", result);
        Assertions.assertNotNull(result);
    }
}
