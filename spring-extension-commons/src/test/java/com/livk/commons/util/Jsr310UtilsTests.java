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

	private static final ZoneId UTC = ZoneId.of("UTC");

	private static final ZoneId TOKYO = ZoneId.of("Asia/Tokyo");

	// --- timestamp ---

	@Test
	void timestamp() {
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		assertThat(Jsr310Utils.timestamp(dt)).isEqualTo(dt.atZone(ZONE).toEpochSecond());
	}

	@Test
	void timestampWithZone() {
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		assertThat(Jsr310Utils.timestamp(dt, UTC)).isEqualTo(dt.atZone(UTC).toEpochSecond());
	}

	@Test
	void timestampMillis() {
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		assertThat(Jsr310Utils.timestampMillis(dt)).isEqualTo(dt.atZone(ZONE).toInstant().toEpochMilli());
	}

	@Test
	void timestampMillisWithZone() {
		LocalDateTime dt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		assertThat(Jsr310Utils.timestampMillis(dt, UTC)).isEqualTo(dt.atZone(UTC).toInstant().toEpochMilli());
	}

	// --- datetime from timestamp ---

	@Test
	void datetimeFromSeconds() {
		long now = Instant.now().getEpochSecond();
		assertThat(Jsr310Utils.datetime(now)).isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochSecond(now), ZONE));
	}

	@Test
	void datetimeFromSecondsWithZone() {
		long now = Instant.now().getEpochSecond();
		assertThat(Jsr310Utils.datetime(now, UTC)).isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochSecond(now), UTC));
	}

	@Test
	void datetimeFromMillis() {
		long now = Instant.now().toEpochMilli();
		assertThat(Jsr310Utils.dateTimeMillis(now)).isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZONE));
	}

	@Test
	void datetimeFromMillisWithZone() {
		long now = Instant.now().toEpochMilli();
		assertThat(Jsr310Utils.dateTimeMillis(now, UTC))
			.isEqualTo(LocalDateTime.ofInstant(Instant.ofEpochMilli(now), UTC));
	}

	// --- format ---

	@Test
	void formatWithPattern() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		assertThat(Jsr310Utils.format(dt, "yyyy/MM/dd HH:mm")).isEqualTo("2025/05/20 12:34");
	}

	@Test
	void formatWithPatternAndZone() {
		Instant instant = Instant.parse("2025-05-20T12:34:56Z");
		assertThat(Jsr310Utils.format(instant, "yyyy-MM-dd HH:mm:ss", UTC)).isEqualTo("2025-05-20 12:34:56");
	}

	@Test
	void formatWithDateTimeFormatter() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		assertThat(Jsr310Utils.format(dt, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))).isEqualTo("20250520123456");
	}

	@Test
	void formatDate() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		assertThat(Jsr310Utils.formatDate(dt)).isEqualTo("2025-05-20");
	}

	@Test
	void formatDateTime() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		assertThat(Jsr310Utils.formatDateTime(dt)).isEqualTo("2025-05-20 12:34:56");
	}

	@Test
	void formatTime() {
		LocalDateTime dt = LocalDateTime.of(2025, 5, 20, 12, 34, 56);
		assertThat(Jsr310Utils.formatTime(dt)).isEqualTo("12:34:56");
	}

	@Test
	void formatDateWithZoneId() {
		Instant instant = Instant.parse("2025-05-20T00:00:00Z");
		assertThat(Jsr310Utils.formatDate(instant, UTC)).isEqualTo("2025-05-20");
		assertThat(Jsr310Utils.formatDate(instant, TOKYO)).isEqualTo("2025-05-20");
	}

	@Test
	void formatDateTimeWithZoneId() {
		Instant instant = Instant.parse("2025-05-20T00:00:00Z");
		assertThat(Jsr310Utils.formatDateTime(instant, UTC)).isEqualTo("2025-05-20 00:00:00");
		assertThat(Jsr310Utils.formatDateTime(instant, TOKYO)).isEqualTo("2025-05-20 09:00:00");
	}

	@Test
	void formatTimeWithZoneId() {
		Instant instant = Instant.parse("2025-05-20T00:00:00Z");
		assertThat(Jsr310Utils.formatTime(instant, UTC)).isEqualTo("00:00:00");
		assertThat(Jsr310Utils.formatTime(instant, TOKYO)).isEqualTo("09:00:00");
	}

	// --- parse ---

	@Test
	void parseDateTimeWithDefaultPattern() {
		assertThat(Jsr310Utils.parseDateTime("2025-05-20 12:34:56"))
			.isEqualTo(LocalDateTime.of(2025, 5, 20, 12, 34, 56));
	}

	@Test
	void parseDateTimeWithCustomPattern() {
		assertThat(Jsr310Utils.parseDateTime("20250520123456", "yyyyMMddHHmmss"))
			.isEqualTo(LocalDateTime.of(2025, 5, 20, 12, 34, 56));
	}

	@Test
	void parseDateWithDefaultPattern() {
		assertThat(Jsr310Utils.parseDate("2025-05-20")).isEqualTo(LocalDate.of(2025, 5, 20));
	}

	@Test
	void parseDateWithCustomPattern() {
		assertThat(Jsr310Utils.parseDate("20250520", "yyyyMMdd")).isEqualTo(LocalDate.of(2025, 5, 20));
	}

	@Test
	void parseTimeWithDefaultPattern() {
		assertThat(Jsr310Utils.parseTime("12:34:56")).isEqualTo(LocalTime.of(12, 34, 56));
	}

	@Test
	void parseTimeWithCustomPattern() {
		assertThat(Jsr310Utils.parseTime("123456", "HHmmss")).isEqualTo(LocalTime.of(12, 34, 56));
	}

	// --- plus / minus ---

	@Test
	void plusDays() {
		LocalDateTime base = LocalDateTime.of(2025, 1, 1, 0, 0);
		assertThat(Jsr310Utils.plusDays(base, 2)).isEqualTo(base.plusDays(2));
	}

	@Test
	void minusDays() {
		LocalDateTime base = LocalDateTime.of(2025, 1, 1, 0, 0);
		assertThat(Jsr310Utils.minusDays(base, 2)).isEqualTo(base.minusDays(2));
	}

	@Test
	void plusHours() {
		LocalDateTime base = LocalDateTime.of(2025, 1, 1, 0, 0);
		assertThat(Jsr310Utils.plusHours(base, 3)).isEqualTo(base.plusHours(3));
	}

	@Test
	void minusHours() {
		LocalDateTime base = LocalDateTime.of(2025, 1, 1, 0, 0);
		assertThat(Jsr310Utils.minusHours(base, 3)).isEqualTo(base.minusHours(3));
	}

	@Test
	void plusMinutes() {
		LocalDateTime base = LocalDateTime.of(2025, 1, 1, 0, 0);
		assertThat(Jsr310Utils.plusMinutes(base, 15)).isEqualTo(base.plusMinutes(15));
	}

	@Test
	void minusMinutes() {
		LocalDateTime base = LocalDateTime.of(2025, 1, 1, 0, 0);
		assertThat(Jsr310Utils.minusMinutes(base, 15)).isEqualTo(base.minusMinutes(15));
	}

	// --- day boundaries ---

	@Test
	void startOfDay() {
		LocalDate date = LocalDate.of(2025, 5, 20);
		assertThat(Jsr310Utils.startOfDay(date)).isEqualTo(date.atStartOfDay());
	}

	@Test
	void endOfDay() {
		LocalDate date = LocalDate.of(2025, 5, 20);
		assertThat(Jsr310Utils.endOfDay(date)).isEqualTo(date.atTime(LocalTime.MAX));
	}

	// --- month boundaries ---

	@Test
	void firstDayOfMonth() {
		assertThat(Jsr310Utils.firstDayOfMonth(LocalDate.of(2025, 2, 15))).isEqualTo(LocalDate.of(2025, 2, 1));
	}

	@Test
	void lastDayOfMonth() {
		assertThat(Jsr310Utils.lastDayOfMonth(LocalDate.of(2025, 2, 15))).isEqualTo(LocalDate.of(2025, 2, 28));
	}

	@Test
	void firstDayOfWeek() {
		LocalDate wednesday = LocalDate.of(2025, 5, 21);
		assertThat(Jsr310Utils.firstDayOfWeek(wednesday).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
	}

	// --- before / after now ---

	@Test
	void isBeforeNow() {
		assertThat(Jsr310Utils.isBeforeNow(LocalDateTime.now().minusDays(1))).isTrue();
		assertThat(Jsr310Utils.isBeforeNow(LocalDateTime.now().plusDays(1))).isFalse();
	}

	@Test
	void isAfterNow() {
		assertThat(Jsr310Utils.isAfterNow(LocalDateTime.now().plusDays(1))).isTrue();
		assertThat(Jsr310Utils.isAfterNow(LocalDateTime.now().minusDays(1))).isFalse();
	}

	// --- between calculations ---

	@Test
	void secondsBetween() {
		LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 1, 2, 1, 1, 1);
		assertThat(Jsr310Utils.secondsBetween(start, end)).isEqualTo(Duration.between(start, end).getSeconds());
	}

	@Test
	void minutesBetween() {
		LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 1, 2, 1, 1, 1);
		assertThat(Jsr310Utils.minutesBetween(start, end)).isEqualTo(Duration.between(start, end).toMinutes());
	}

	@Test
	void hoursBetween() {
		LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 1, 2, 1, 1, 1);
		assertThat(Jsr310Utils.hoursBetween(start, end)).isEqualTo(Duration.between(start, end).toHours());
	}

	@Test
	void daysBetween() {
		LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 1, 2, 1, 1, 1);
		assertThat(Jsr310Utils.daysBetween(start, end)).isEqualTo(ChronoUnit.DAYS.between(start, end));
	}

}
