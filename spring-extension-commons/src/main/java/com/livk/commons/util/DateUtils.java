/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.util;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * DateUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * The constant YMD.
     */
    public static final String YMD = "yyyy-MM-dd";

    /**
     * The constant HMS.
     */
    public static final String HMS = "HH:mm:ss";

    /**
     * The constant YMD_HMS.
     */
    public static final String YMD_HMS = YMD + " " + HMS;

    /**
     * LocalDateTime 转时间戳
     *
     * @param localDateTime the local date time
     * @return the long
     */
    public static Long timestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timeStamp the time stamp
     * @return the local date time
     */
    public static LocalDateTime localDateTime(Long timeStamp) {
        return LocalDateTime.ofEpochSecond(timeStamp, 0, OffsetDateTime.now().getOffset());
    }

    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime the local date time
     * @return the date
     */
    public static Date date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 转 Date
     *
     * @param localDate the local date
     * @return the date
     */
    public static Date date(LocalDate localDate) {
        return date(localDate.atTime(LocalTime.now(ZoneId.systemDefault())));
    }

    /**
     * Date转 LocalDateTime
     *
     * @param date the date
     * @return the local date time
     */
    public static LocalDateTime localDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime the local date time
     * @param patten        the patten
     * @return the string
     */
    public static String format(LocalDateTime localDateTime, String patten) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patten);
        return format(localDateTime, formatter);
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime the local date time
     * @param formatter     the formatter
     * @return the string
     */
    public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return formatter.format(localDateTime);
    }

    /**
     * 字符串转 LocalDateTime
     *
     * @param localDateTime the local date time
     * @param pattern       the pattern
     * @return the local date time
     */
    public static LocalDateTime parse(String localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(localDateTime, dateTimeFormatter);
    }
}
