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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * JSR310 日期、时间相关工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class LocalDateTimeUtils {

	/**
	 * YMD.
	 */
	public static final String YMD = "yyyy-MM-dd";

	/**
	 * HMS.
	 */
	public static final String HMS = "HH:mm:ss";

	/**
	 * YMD_HMS.
	 */
	public static final String YMD_HMS = YMD + " " + HMS;

	/**
	 * LocalDateTime 转时间戳
	 * @param localDateTime the local date time
	 * @return the long
	 */
	public static Long timestamp(LocalDateTime localDateTime) {
		return timestamp(localDateTime, ZoneId.systemDefault());
	}

	/**
	 * LocalDateTime 转时间戳
	 * @param localDateTime the local date time
	 * @param zoneId zoneId
	 * @return the long
	 */
	public static Long timestamp(LocalDateTime localDateTime, ZoneId zoneId) {
		return localDateTime.atZone(zoneId).toEpochSecond();
	}

	/**
	 * 时间戳转LocalDateTime
	 * @param timeStamp the time stamp
	 * @return the local date time
	 */
	public static LocalDateTime localDateTime(Long timeStamp) {
		return localDateTime(timeStamp, ZoneId.systemDefault());
	}

	/**
	 * 时间戳转LocalDateTime
	 * @param timeStamp the time stamp
	 * @param zoneId zoneId
	 * @return the local date time
	 */
	public static LocalDateTime localDateTime(Long timeStamp, ZoneId zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), zoneId);
	}

	/**
	 * 日期 格式化
	 * @param localDateTime the local date time
	 * @param patten the patten
	 * @return the string
	 */
	public static String format(LocalDateTime localDateTime, String patten) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patten);
		return format(localDateTime, formatter);
	}

	/**
	 * 日期 格式化
	 * @param localDateTime the local date time
	 * @param patten the patten
	 * @param zoneId zoneId
	 * @return the string
	 */
	public static String format(LocalDateTime localDateTime, String patten, ZoneId zoneId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patten).withZone(zoneId);
		return format(localDateTime, formatter);
	}

	/**
	 * 日期 格式化
	 * @param localDateTime the local date time
	 * @param formatter the formatter
	 * @return the string
	 */
	public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
		return formatter.format(localDateTime);
	}

	/**
	 * 字符串转 LocalDateTime
	 * @param localDateTime the local date time
	 * @param pattern the pattern
	 * @return the local date time
	 */
	public static LocalDateTime parse(String localDateTime, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(localDateTime, dateTimeFormatter);
	}

	/**
	 * 字符串转 LocalDateTime
	 * @param localDateTime the local date time
	 * @param pattern the pattern
	 * @param zoneId zoneId
	 * @return the local date time
	 */
	public static LocalDateTime parse(String localDateTime, String pattern, ZoneId zoneId) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
		return LocalDateTime.parse(localDateTime, dateTimeFormatter);
	}

}
