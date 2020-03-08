package com.ticketadvantage.services.dao.sites.donbest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TestDate {
	private final static DateTimeFormatter inputDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'", Locale.ENGLISH);

	/**
	 * 
	 */
	public TestDate() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final ZoneId arrivingZone = ZoneId.of("America/Los_Angeles");
		final LocalDateTime localdatetime = LocalDateTime.parse("2018-08-26T02:00Z", inputDateTimeFormatter);
		final ZonedDateTime utcZoned = localdatetime.atZone(ZoneId.of("UTC"));
		final ZonedDateTime laDateTime = utcZoned.withZoneSameInstant(arrivingZone);
		final LocalDate localDate = laDateTime.toLocalDate();
		final LocalTime localTime = laDateTime.toLocalTime();
		final LocalDateTime localDateTime = laDateTime.toLocalDateTime();
		System.out.println("localDateTime: " + localDateTime);
		System.out.println("localDate: " + localDate);
		System.out.println("localTime: " + localTime);
	}
}
