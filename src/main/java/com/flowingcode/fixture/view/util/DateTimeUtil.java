package com.flowingcode.fixture.view.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
	
}
