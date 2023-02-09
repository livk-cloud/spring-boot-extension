package com.livk.commons.util;

import com.livk.commons.test.TestLogUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * DateUtilsTest
 * </p>
 *
 * @author livk
 */
class DateUtilsTest {

    @Test
    void timestampTest() {
        Long result = DateUtils.timestamp(LocalDateTime.now());
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }

    @Test
    void localDateTimeTest() {
        LocalDateTime result = DateUtils.localDateTime(1663063303L);
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }

    @Test
    void dateTest() {
        Date result = DateUtils.date(LocalDate.now());
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }

    @Test
    void testDateTest() {
        Date result = DateUtils.date(LocalDateTime.now());
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }

    @Test
    void toLocalDateTimeTest() {
        LocalDateTime result = DateUtils.localDateTime(new Date());
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }

    @Test
    void formatTest() {
        String result = DateUtils.format(LocalDateTime.now(), DateTimeFormatter.ofPattern(DateUtils.YMD_HMS));
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }

    @Test
    void testFormatTest() {
        String result = DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS);
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }

    @Test
    void parseTest() {
        LocalDateTime result = DateUtils.parse("2022-09-13 18:05:12", DateUtils.YMD_HMS);
        TestLogUtils.info("result:{}", result);
        assertNotNull(result);
    }
}
