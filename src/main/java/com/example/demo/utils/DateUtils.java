package com.example.demo.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtils {
	
	public static final String DATE_FORMAT_yyyy_MM_ddTHH_mm_ssZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static Timestamp getTimestamp(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_yyyy_MM_ddTHH_mm_ssZ);
		try {
			simpleDateFormat.setTimeZone(TimeZone.getDefault());
			Timestamp timestamp = new Timestamp(simpleDateFormat.parse(date).getTime());
			return timestamp;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
