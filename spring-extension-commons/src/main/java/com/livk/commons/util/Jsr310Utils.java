/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.util;

import lombok.experimental.UtilityClass;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * JSR310 日期、时间相关工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class Jsr310Utils {

	private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

	/**
	 * 标准日期格式：yyyy-MM-dd
	 */
	public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 标准时间格式：HH:mm:ss
	 */
	public static final String NORM_TIME_PATTERN = "HH:mm:ss";

	/**
	 * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
	 */
	public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static DateTimeFormatter getFormatter(String pattern) {
		return FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
	}

	/**
	 * LocalDateTime 转秒时间戳
	 * @param dateTime the datetime
	 * @return the long
	 */
	public static Long timestamp(LocalDateTime dateTime) {
		return timestamp(dateTime, ZoneId.systemDefault());
	}

	/**
	 * LocalDateTime 转秒时间戳
	 * @param dateTime the datetime
	 * @param zoneId zoneId
	 * @return the long
	 */
	public static Long timestamp(LocalDateTime dateTime, ZoneId zoneId) {
		return dateTime.atZone(zoneId).toEpochSecond();
	}

	/**
	 * LocalDateTime 转毫秒时间戳
	 * @param dateTime the datetime
	 * @return the long
	 */
	public static long timestampMillis(LocalDateTime dateTime) {
		return timestampMillis(dateTime, ZoneId.systemDefault());
	}

	/**
	 * LocalDateTime 转毫秒时间戳
	 * @param dateTime the datetime
	 * @param zoneId zoneId
	 * @return the long
	 */
	public static long timestampMillis(LocalDateTime dateTime, ZoneId zoneId) {
		return dateTime.atZone(zoneId).toInstant().toEpochMilli();
	}

	/**
	 * 秒时间戳转LocalDateTime
	 * @param timeStamp the timestamp
	 * @return the datetime
	 */
	public static LocalDateTime datetime(Long timeStamp) {
		return datetime(timeStamp, ZoneId.systemDefault());
	}

	/**
	 * 秒时间戳转LocalDateTime
	 * @param timeStamp the timestamp
	 * @param zoneId zoneId
	 * @return the datetime
	 */
	public static LocalDateTime datetime(Long timeStamp, ZoneId zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), zoneId);
	}

	/**
	 * 毫秒时间戳转LocalDateTime
	 * @param timestampMillis the timestamp mills
	 * @return the datetime
	 */
	public static LocalDateTime dateTimeMillis(long timestampMillis) {
		return dateTimeMillis(timestampMillis, ZoneId.systemDefault());
	}

	/**
	 * 毫秒时间戳转LocalDateTime
	 * @param timestampMillis the timestamp mills
	 * @param zoneId zoneId
	 * @return the datetime
	 */
	public static LocalDateTime dateTimeMillis(long timestampMillis, ZoneId zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), zoneId);
	}

	/**
	 * 日期 格式化
	 * @param temporal the datetime
	 * @param pattern the patten
	 * @return the string
	 */
	public static String format(Temporal temporal, String pattern) {
		DateTimeFormatter formatter = FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
		return format(temporal, formatter);
	}

	/**
	 * 日期 格式化
	 * @param temporal the datetime
	 * @param pattern the patten
	 * @param zoneId zoneId
	 * @return the string
	 */
	public static String format(Temporal temporal, String pattern, ZoneId zoneId) {
		DateTimeFormatter formatter = FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern)
			.withZone(zoneId);
		return format(temporal, formatter);
	}

	/**
	 * 日期 格式化
	 * @param temporal the datetime
	 * @param formatter the formatter
	 * @return the string
	 */
	public static String format(Temporal temporal, DateTimeFormatter formatter) {
		return formatter.format(temporal);
	}

	/**
	 * 日期 格式化(yyyy-MM-dd)
	 * @param temporal the datetime
	 * @return the string
	 */
	public static String formatDate(Temporal temporal) {
		return format(temporal, NORM_DATE_PATTERN);
	}

	/**
	 * 日期 格式化(yyyy-MM-dd)
	 * @param temporal the datetime
	 * @param zoneId zoneId
	 * @return the string
	 */
	public static String formatDate(Temporal temporal, ZoneId zoneId) {
		return format(temporal, NORM_DATE_PATTERN, zoneId);
	}

	/**
	 * 日期 格式化(yyyy-MM-dd HH:mm:ss)
	 * @param temporal the datetime
	 * @return the string
	 */
	public static String formatDateTime(Temporal temporal) {
		return format(temporal, NORM_DATETIME_PATTERN);
	}

	/**
	 * 日期 格式化(yyyy-MM-dd HH:mm:ss)
	 * @param temporal the datetime
	 * @param zoneId zoneId
	 * @return the string
	 */
	public static String formatDateTime(Temporal temporal, ZoneId zoneId) {
		return format(temporal, NORM_DATETIME_PATTERN, zoneId);
	}

	/**
	 * 日期 格式化(HH:mm:ss)
	 * @param temporal the datetime
	 * @return the string
	 */
	public static String formatTime(Temporal temporal) {
		return format(temporal, NORM_TIME_PATTERN);
	}

	/**
	 * 日期 格式化(HH:mm:ss)
	 * @param temporal the datetime
	 * @param zoneId zoneId
	 * @return the string
	 */
	public static String formatTime(Temporal temporal, ZoneId zoneId) {
		return format(temporal, NORM_TIME_PATTERN, zoneId);
	}

	/**
	 * 字符串转 LocalDateTime
	 * @param datetime the datetimeStr
	 * @param pattern the pattern
	 * @return the datetime
	 */
	public static LocalDateTime parseDateTime(String datetime, String pattern) {
		DateTimeFormatter dateTimeFormatter = getFormatter(pattern);
		return LocalDateTime.parse(datetime, dateTimeFormatter);
	}

	/**
	 * 字符串转 LocalDateTime
	 * @param datetime the datetimeStr
	 * @param pattern the pattern
	 * @param zoneId zoneId
	 * @return the datetime
	 */
	public static LocalDateTime parseDateTime(String datetime, String pattern, ZoneId zoneId) {
		DateTimeFormatter dateTimeFormatter = getFormatter(pattern).withZone(zoneId);
		return LocalDateTime.parse(datetime, dateTimeFormatter);
	}

	/**
	 * 字符串转 LocalDateTime (yyyy-MM-dd HH:mm:ss)
	 * @param datetime the datetimeStr
	 * @return the datetime
	 */
	public static LocalDateTime parseDateTime(String datetime) {
		return parseDateTime(datetime, NORM_DATETIME_PATTERN);
	}

	/**
	 * 字符串转 LocalDate (yyyy-MM-dd)
	 * @param date the dateStr
	 * @return the date
	 */
	public static LocalDate parseDate(String date) {
		return parseDate(date, NORM_DATE_PATTERN);
	}

	/**
	 * 字符串转 LocalDate
	 * @param date the dateStr
	 * @param pattern the pattern
	 * @return the date
	 */
	public static LocalDate parseDate(String date, String pattern) {
		return parseDate(date, pattern, ZoneId.systemDefault());
	}

	/**
	 * 字符串转 LocalDate
	 * @param date the dateStr
	 * @param pattern the pattern
	 * @param zoneId zoneId
	 * @return the date
	 */
	public static LocalDate parseDate(String date, String pattern, ZoneId zoneId) {
		DateTimeFormatter dateTimeFormatter = getFormatter(pattern).withZone(zoneId);
		return LocalDate.parse(date, dateTimeFormatter);
	}

	/**
	 * 字符串转 LocalTime (HH:mm:ss)
	 * @param time the timeStr
	 * @return the datetime
	 */
	public static LocalTime parseTime(String time) {
		return parseTime(time, NORM_TIME_PATTERN);
	}

	/**
	 * 字符串转 LocalTime
	 * @param time the timeStr
	 * @param pattern the pattern
	 * @return the date
	 */
	public static LocalTime parseTime(String time, String pattern) {
		return parseTime(time, pattern, ZoneId.systemDefault());
	}

	/**
	 * 字符串转 LocalTime
	 * @param time the timeStr
	 * @param pattern the pattern
	 * @param zoneId zoneId
	 * @return the datetime
	 */
	public static LocalTime parseTime(String time, String pattern, ZoneId zoneId) {
		DateTimeFormatter dateTimeFormatter = getFormatter(pattern).withZone(zoneId);
		return LocalTime.parse(time, dateTimeFormatter);
	}

	/**
	 * 指定时间增加指定天数。
	 * @param time 要操作的时间
	 * @param days 增加的天数（负数表示减少）
	 * @return 增加天数后的时间
	 */
	public static LocalDateTime plusDays(LocalDateTime time, long days) {
		return time.plusDays(days);
	}

	/**
	 * 指定时间减少指定天数。
	 * @param time 要操作的时间
	 * @param days 减少的天数（负数表示增加）
	 * @return 减少天数后的时间
	 */
	public static LocalDateTime minusDays(LocalDateTime time, long days) {
		return time.minusDays(days);
	}

	/**
	 * 指定时间增加指定小时数。
	 * @param time 要操作的时间
	 * @param hours 增加的小时数
	 * @return 增加小时后的时间
	 */
	public static LocalDateTime plusHours(LocalDateTime time, long hours) {
		return time.plusHours(hours);
	}

	/**
	 * 指定时间减少指定小时数。
	 * @param time 要操作的时间
	 * @param hours 减少的小时数
	 * @return 减少小时后的时间
	 */
	public static LocalDateTime minusHours(LocalDateTime time, long hours) {
		return time.minusHours(hours);
	}

	/**
	 * 指定时间增加指定分钟数。
	 * @param time 要操作的时间
	 * @param minutes 增加的分钟数
	 * @return 增加分钟后的时间
	 */
	public static LocalDateTime plusMinutes(LocalDateTime time, long minutes) {
		return time.plusMinutes(minutes);
	}

	/**
	 * 指定时间减少指定分钟数。
	 * @param time 要操作的时间
	 * @param minutes 减少的分钟数
	 * @return 减少分钟后的时间
	 */
	public static LocalDateTime minusMinutes(LocalDateTime time, long minutes) {
		return time.minusMinutes(minutes);
	}

	/**
	 * 获取指定日期的开始时间（当天 00:00:00）。
	 * @param date 指定日期
	 * @return 对应的 LocalDateTime
	 */
	public static LocalDateTime startOfDay(LocalDate date) {
		return date.atStartOfDay();
	}

	/**
	 * 获取指定日期的结束时间（当天 23:59:59.999999999）。
	 * @param date 指定日期
	 * @return 对应的 LocalDateTime
	 */
	public static LocalDateTime endOfDay(LocalDate date) {
		return date.atTime(LocalTime.MAX);
	}

	/**
	 * 获取指定日期所在月的第一天。
	 * @param date 指定日期
	 * @return 当月第一天
	 */
	public static LocalDate firstDayOfMonth(LocalDate date) {
		return date.withDayOfMonth(1);
	}

	/**
	 * 获取指定日期所在月的最后一天。
	 * @param date 指定日期
	 * @return 当月最后一天
	 */
	public static LocalDate lastDayOfMonth(LocalDate date) {
		return date.withDayOfMonth(date.lengthOfMonth());
	}

	/**
	 * 获取指定日期所在周的周一日期。
	 * @param date 指定日期
	 * @return 当前周的周一
	 */
	public static LocalDate firstDayOfWeek(LocalDate date) {
		return date.with(DayOfWeek.MONDAY);
	}

	/**
	 * 判断给定时间是否早于当前系统时间。
	 * @param time 要比较的时间
	 * @return 如果早于当前时间返回 true，否则 false
	 */
	public static boolean isBeforeNow(LocalDateTime time) {
		return time.isBefore(LocalDateTime.now());
	}

	/**
	 * 判断给定时间是否晚于当前系统时间。
	 * @param time 要比较的时间
	 * @return 如果晚于当前时间返回 true，否则 false
	 */
	public static boolean isAfterNow(LocalDateTime time) {
		return time.isAfter(LocalDateTime.now());
	}

	/**
	 * 计算两个时间之间相差的秒数。
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return 相差的秒数（end - start）
	 */
	public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
		return ChronoUnit.SECONDS.between(start, end);
	}

	/**
	 * 计算两个时间之间相差的分钟数。
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return 相差的分钟数（end - start）
	 */
	public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
		return ChronoUnit.MINUTES.between(start, end);
	}

	/**
	 * 计算两个时间之间相差的小时数。
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return 相差的小时数（end - start）
	 */
	public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
		return ChronoUnit.HOURS.between(start, end);
	}

	/**
	 * 计算两个时间之间相差的天数。
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return 相差的天数（end - start）
	 */
	public static long daysBetween(LocalDateTime start, LocalDateTime end) {
		return ChronoUnit.DAYS.between(start, end);
	}

}
