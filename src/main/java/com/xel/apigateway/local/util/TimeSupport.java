package com.xel.apigateway.local.util;

import java.time.LocalDateTime;


public class TimeSupport {
	// 2018-09-13 13:11:13
	public String getRemainingTime(String time, boolean withText) {

		String result = null;

		CurrentDate cd = new CurrentDate();
		LocalDateTime currentTime = cd.getCurrentTime();
		LocalDateTime inputTime = cd.stringToLocalDateTime(time);

		boolean yearStatus = true;
		boolean monthStatus = true;
		boolean dayStatus = true;
		boolean hourStatus = true;
		boolean minuteStatus = true;
		boolean secondStatus = true;
		
		if (currentTime.getYear() == inputTime.getYear()) {
			yearStatus = false;
		}
		if (currentTime.getMonthValue() == inputTime.getMonthValue()) {
			monthStatus = false;
		}
		if (currentTime.getDayOfMonth() == inputTime.getDayOfMonth()) {
			dayStatus = false;
		}
		if (currentTime.getHour() == inputTime.getHour()) {
			hourStatus = false;
		}
		if (currentTime.getMinute() == inputTime.getMinute()) {
			minuteStatus = false;
		}
		if (currentTime.getSecond() == inputTime.getSecond()) {
			secondStatus = false;
		}
		
		LocalDateTime timeOutput = currentTime;

		int seconds = 0;
		if (secondStatus) {
			timeOutput = timeOutput.minusSeconds(inputTime.getSecond());
			seconds = timeOutput.getSecond();
		}
		int minutes = 0;
		if (minuteStatus) {
			timeOutput = timeOutput.minusMinutes(inputTime.getMinute());
			minutes = timeOutput.getMinute();
		}
		int hours = 0;
		if (hourStatus) {
			timeOutput = timeOutput.minusHours(inputTime.getHour());
			hours = timeOutput.getHour();
		}
		int days = 0;
		if (dayStatus) {
			timeOutput = timeOutput.minusDays(inputTime.getDayOfMonth());
			days = timeOutput.getDayOfMonth();
		}
		int months = 0;
		if (monthStatus) {
			timeOutput = timeOutput.minusMonths(inputTime.getMonthValue());
			months = timeOutput.getMonthValue();
		}
		int years = 0;
		if (yearStatus) {
			timeOutput = timeOutput.minusYears(inputTime.getYear());
			years = timeOutput.getYear();
		}

		if (withText) {
			StringBuilder sb = new StringBuilder();
			int cacheSeconds = 5;

			if (years > 0) {
				if (years > 1) {
					sb.append(years).append(" ").append("years ago");
				} else {
					sb.append(years).append(" ").append("year ago");
				}
			} else if (months > 0) {
				if (months > 1) {
					sb.append(months).append(" ").append("months ago");
				} else {
					sb.append(months).append(" ").append("month ago");
				}
			} else if (days > 0) {
				if (days > 1) {
					sb.append(days).append(" ").append("days ago");
				} else {
					sb.append(days).append(" ").append("day ago");
				}
			} else if (hours > 0) {
				if (hours > 1) {
					sb.append(hours).append(" ").append("hours ago");
				} else {
					sb.append(hours).append(" ").append("hour ago");
				}
			} else if (minutes > 0) {
				if (minutes > 1) {
					sb.append(minutes).append(" ").append("minutes ago");
				} else {
					sb.append(minutes).append(" ").append("minute ago");
				}
			} else if (seconds > -1) {
				if (seconds <= cacheSeconds) {
					sb.append("just now");
				} else {
					if (seconds > 1) {
						sb.append(seconds).append(" ").append("seconds ago");
					} else {
						sb.append(seconds).append(" ").append("second ago");
					}
				}
			}
			result = sb.toString();
		}

		return result;
	}
}
