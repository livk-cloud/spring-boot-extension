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

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class Jsr310UtilsTests {

	private static final ZoneId ZONE = ZoneId.systemDefault();

	@Test
	void testTimestamp() {
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		long seconds = Jsr310Utils.timestamp(dt);
		long expected = dt.atZone(ZONE).toEpochSecond();

		assertThat(seconds).isEqualTo(expected);
	}

	@Test
	void testTimestampWithZone() {
		ZoneId utc = ZoneId.of("UTC");
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		long seconds = Jsr310Utils.timestamp(dt, utc);
		assertThat(seconds).isEqualTo(dt.atZone(utc).toEpochSecond());
	}

	@Test
	void testTimestampMillis() {
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		long millis = Jsr310Utils.timestampMillis(dt);
		assertThat(millis).isEqualTo(dt.atZone(ZONE).toInstant().toEpochMilli());
	}

	@Test
	void testTimestampMillisWithZone() {
		ZoneId utc = ZoneId.of("UTC");
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		long millis = Jsr310Utils.timestampMillis(dt, utc);
		assertThat(millis).isEqualTo(dt.atZone(utc).toInstant().toEpochMilli());
	}

	@Test
	void testDatetime() {
		long now = Instant.now().getEpochSecond();
		assertThat(Jsr310Utils.datetime(now)).isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochSecond(now), ZONE));
	}

	@Test
	void testDatetimeWithZone() {
		long now = Instant.now().getEpochSecond();
		ZoneId utc = ZoneId.of("UTC");
		assertThat(Jsr310Utils.datetime(now, utc)).isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochSecond(now), utc));
	}

	@Test
	void testDatetimeMillis() {
		long now = Instant.now().toEpochMilli();
		assertThat(Jsr310Utils.dateTimeMillis(now)).isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZONE));
	}

	@Test
	void testDatetimeMillisWithZone() {
		long now = Instant.now().toEpochMilli();
		ZoneId utc = ZoneId.of("UTC");
		assertThat(Jsr310Utils.dateTimeMillis(now, utc))
			.isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochMilli(now), utc));
	}

	@Test
	void testFormatWithPattern() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		String formatted = Jsr310Utils.format(dt, "yyyy/MM/dd HH:mm");
		assertThat(formatted).isEqualTo("2025/05/20 12:34");
	}

	@Test
	void testFormatWithPatternAndZone() {
		Instant instant = Instant.parse("2025-05-20T12:34:56Z");
		String formatted = Jsr310Utils.format(instant, "yyyy-MM-dd HH:mm:ss", ZoneId.of("UTC"));
		assertThat(formatted).isEqualTo("2025-05-20 12:34:56");
	}

	@Test
	void testFormatWithGetFormatter() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		String formatted = Jsr310Utils.format(dt, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		assertThat(formatted).isEqualTo("20250520123456");
	}

	@Test
	void testFormatShortcuts() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		assertThat(Jsr310Utils.formatDate(dt)).isEqualTo("2025-05-20");
		assertThat(Jsr310Utils.formatDateTime(dt)).isEqualTo("2025-05-20 12:34:56");
		assertThat(Jsr310Utils.formatTime(dt)).isEqualTo("12:34:56");
	}

	@Test
	void testParseDateTimeShortcuts() {
		assertThat(Jsr310Utils.parseDateTime("2025-05-20 12:34:56"))
			.isEqualTo(LocalDateTime.of(2025, 5, 20, 12, 34, 56));

		assertThat(Jsr310Utils.parseDate("2025-05-20")).isEqualTo(LocalDate.of(2025, 5, 20));

		assertThat(Jsr310Utils.parseTime("12:34:56")).isEqualTo(LocalTime.of(12, 34, 56));
	}

	@Test
	void testPlusMinus() {
		LocalDateTime base = LocalDateTime.of(2025, 1, 1, 0, 0);
		assertThat(Jsr310Utils.plusDays(base, 2)).isEqualTo(base.plusDays(2));
		assertThat(Jsr310Utils.minusDays(base, 2)).isEqualTo(base.minusDays(2));
		assertThat(Jsr310Utils.plusHours(base, 3)).isEqualTo(base.plusHours(3));
		assertThat(Jsr310Utils.minusHours(base, 3)).isEqualTo(base.minusHours(3));
		assertThat(Jsr310Utils.plusMinutes(base, 15)).isEqualTo(base.plusMinutes(15));
		assertThat(Jsr310Utils.minusMinutes(base, 15)).isEqualTo(base.minusMinutes(15));
	}

	@Test
	void testStartAndEndOfDay() {
		LocalDate date = LocalDate.of(2025, 5, 20);
		assertThat(Jsr310Utils.startOfDay(date)).isEqualTo(date.atStartOfDay());
		assertThat(Jsr310Utils.endOfDay(date)).isEqualTo(date.atTime(LocalTime.MAX));
	}

	@Test
	void testMonthBoundaries() {
		LocalDate date = LocalDate.of(2025, 2, 15);
		assertThat(Jsr310Utils.firstDayOfMonth(date)).isEqualTo(LocalDate.of(2025, 2, 1));
		assertThat(Jsr310Utils.lastDayOfMonth(date)).isEqualTo(LocalDate.of(2025, 2, 28));
	}

	@Test
	void testFirstDayOfWeek() {
		LocalDate wednesday = LocalDate.of(2025, 5, 21);
		LocalDate monday = Jsr310Utils.firstDayOfWeek(wednesday);
		assertThat(monday.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
	}

	@Test
	void testBeforeAfterNow() {
		LocalDateTime past = LocalDateTime.now().minusDays(1);
		LocalDateTime future = LocalDateTime.now().plusDays(1);

		assertThat(Jsr310Utils.isBeforeNow(past)).isTrue();
		assertThat(Jsr310Utils.isAfterNow(future)).isTrue();
	}

	@Test
	void testBetweenCalculations() {
		LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 1, 2, 1, 1, 1);

		assertThat(Jsr310Utils.secondsBetween(start, end)).isEqualTo(Duration.between(start, end).getSeconds());
		assertThat(Jsr310Utils.minutesBetween(start, end)).isEqualTo(Duration.between(start, end).toMinutes());
		assertThat(Jsr310Utils.hoursBetween(start, end)).isEqualTo(Duration.between(start, end).toHours());
		assertThat(Jsr310Utils.daysBetween(start, end)).isEqualTo(ChronoUnit.DAYS.between(start, end));
	}

	@Test
	void testFormatDateWithZoneId() {
		// UTC 时间 2025-05-20T00:00Z，在东京（+9）应是 2025-05-20 09:00
		Instant instant = Instant.parse("2025-05-20T00:00:00Z");
		ZoneId utc = ZoneId.of("UTC");
		ZoneId tokyo = ZoneId.of("Asia/Tokyo");

		String utcDate = Jsr310Utils.formatDate(instant, utc);
		String tokyoDate = Jsr310Utils.formatDate(instant, tokyo);

		assertThat(utcDate).isEqualTo("2025-05-20");
		// 注意：在东京是当天早上9点，仍是同一天
		assertThat(tokyoDate).isEqualTo("2025-05-20");
	}

	@Test
	void testFormatDateTimeWithZoneId() {
		Instant instant = Instant.parse("2025-05-20T00:00:00Z");
		ZoneId utc = ZoneId.of("UTC");
		ZoneId tokyo = ZoneId.of("Asia/Tokyo");

		String utcDateTime = Jsr310Utils.formatDateTime(instant, utc);
		String tokyoDateTime = Jsr310Utils.formatDateTime(instant, tokyo);

		assertThat(utcDateTime).isEqualTo("2025-05-20 00:00:00");
		// UTC+9 → 09:00:00
		assertThat(tokyoDateTime).isEqualTo("2025-05-20 09:00:00");
	}

	@Test
	void testFormatTimeWithZoneId() {
		Instant instant = Instant.parse("2025-05-20T00:00:00Z");
		ZoneId utc = ZoneId.of("UTC");
		ZoneId tokyo = ZoneId.of("Asia/Tokyo");

		String utcTime = Jsr310Utils.formatTime(instant, utc);
		String tokyoTime = Jsr310Utils.formatTime(instant, tokyo);

		assertThat(utcTime).isEqualTo("00:00:00");
		assertThat(tokyoTime).isEqualTo("09:00:00");
	}

}
