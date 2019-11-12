package com.xel.apigateway.local.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CurrentDate {

	private String formatPattern = "yyyy-MM-dd HH:mm:ss";
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);

	public LocalDateTime getCurrentTime() {
		return LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithYears(int years) {
		return LocalDateTime.now().plusYears(years).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithMonths(int months) {
		return LocalDateTime.now().plusMonths(months).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithWeeks(int weeks) {
		return LocalDateTime.now().plusWeeks(weeks).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithDays(int days) {
		return LocalDateTime.now().plusDays(days).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithHours(int hours) {
		return LocalDateTime.now().plusHours(hours).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithMinutes(int minutes) {
		return LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithSeconds(int seconds) {
		return LocalDateTime.now().plusSeconds(seconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithNanos(int nanos) {
		return LocalDateTime.now().plusNanos(nanos).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public LocalDateTime getWithDate(int years, int months, int days) {
		return LocalDateTime.now().plusYears(years).plusMonths(months).plusDays(days).atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}

	public LocalDateTime getWithTimestamp(int years, int months, int days, int hours, int minutes, int seconds) {
		return LocalDateTime.now().plusYears(years).plusMonths(months).plusDays(days).plusHours(hours)
				.plusMinutes(minutes).plusSeconds(seconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public Timestamp toTimestamp(LocalDateTime date) {
		return Timestamp.valueOf(date.format(formatter));
	}

	public Timestamp toTimestamp(ZonedDateTime date) {
		return Timestamp.valueOf(date.format(formatter));
	}
	
	public String dateToString(LocalDateTime date) {
		return date.format(formatter);
	}

	public Date toDate(LocalDateTime date) {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}

	public String dateToString(Date date) {
		SimpleDateFormat dt = new SimpleDateFormat(formatPattern);
		return dt.format(date);
	}

	public void setFormat(String format) {
		this.formatPattern = format;
	}

	public LocalDateTime stringToLocalDateTime(String timeStr) {
		return LocalDateTime.parse(timeStr, formatter);
	}

	public LocalDateTime epochToLocalDateTime(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
	}

}
