package com.flowingcode.fixture.view.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTimeUtil {

	private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM/dd/y");
	private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("hh:mma");

	public static String styleDate(ZonedDateTime zoneDate) {
		return zoneDate.format(DATE);
    }

	public static String styleTime(ZonedDateTime zoneDate) {
	return zoneDate.format(TIME);
	}

	public static String styleDate(LocalDate localDate) {
		return localDate.format(DATE);
	}

	/** Time of {@code instant} shown in the viewer's zone and locale (e.g. "21:00" / "9:00 PM"). */
	public static String time(ZonedDateTime instant, ZoneId zone, Locale locale) {
		return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
				.withLocale(locale).format(instant.withZoneSameInstant(zone));
	}

	/** Date of {@code instant} shown in the viewer's zone and locale. */
	public static String date(ZonedDateTime instant, ZoneId zone, Locale locale) {
		return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(locale).format(instant.withZoneSameInstant(zone));
	}

	/** Date and time of {@code instant} shown in the viewer's zone and locale. */
	public static String dateTime(ZonedDateTime instant, ZoneId zone, Locale locale) {
		return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
				.withLocale(locale).format(instant.withZoneSameInstant(zone));
	}

}
