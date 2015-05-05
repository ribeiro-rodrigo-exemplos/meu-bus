package br.com.m2m.meuonibus.activities.helpers;import java.text.ParseException;import java.text.SimpleDateFormat;import java.util.Date;import java.util.concurrent.TimeUnit;public class DateTimeHelper {	public static String formatHoursMinutes(String minutes) {		String time = " - min";		int timeInt = -1;				try {			timeInt = Integer.parseInt(minutes);		} catch (NumberFormatException e) {		}		if(timeInt == -1){			return "-";		}		long ms = timeInt * 60000;		if (ms >= 60000*60) {		time = String.format(				"%d h %02d min",				TimeUnit.MILLISECONDS.toHours(ms),				TimeUnit.MILLISECONDS.toMinutes(ms)						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS								.toHours(ms)));		} else {			return String.format("%02d min", timeInt);		}		return time;	}		public static String formatDateTime(String pubDate){		return getDateTimeString(pubDate,"yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");	}		public static String getDateTimeString(String pubDate, String inputFormat, String outputFormat){		Date _date = null;	 		SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat);		try {		    _date = dateFormat.parse(pubDate);		} catch (ParseException e) {			return pubDate;		}		   		SimpleDateFormat targetFormat = new SimpleDateFormat(outputFormat);		return targetFormat.format(_date);	}		public static String getDateTimeString(Date pubDate, String format){	     SimpleDateFormat shortenedDateFormat = new SimpleDateFormat(format);	     return shortenedDateFormat.format(pubDate);	}}