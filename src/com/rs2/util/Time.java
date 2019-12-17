package com.rs2.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;

public class Time {

	public static int daysBetween(long time1, long time2) {
		DateTime past=new DateTime(time1);
		DateTime today=new DateTime(time2);
		int days = Days.daysBetween(past.toDateMidnight(), today.toDateMidnight()).getDays();
	    return days;
	}
	
	public static boolean isStartOfWeek(long time1) {
		DateTime today=new DateTime(time1);
		return today.dayOfWeek().get() == 1;
	}
	
	public static long getDuration(long time1, long time2) {
		DateTime past=new DateTime(time1);
		DateTime today=new DateTime(time2);
		long duration = new Duration(past, today).getMillis();
		return duration;
	}
	
	public static int hoursBetween(long time1, long time2) {
		DateTime past=new DateTime(time1);
		DateTime today=new DateTime(time2);
		int hours = Hours.hoursBetween(past, today).getHours();
	    return hours;
	}
	
	public static long addHours(long time1, int hours) {
		DateTime today=new DateTime(time1);
		today = today.plusHours(hours);
		return today.getMillis();
	}
	
	public static long getMillisFromDate(int day, int month, int year, int hour, int minute){
		Calendar calendar = new GregorianCalendar(year,month-1,day,hour,minute,0);
		return calendar.getTimeInMillis();
	}
	
	public static String getDate(long time) {
		DateTime past=new DateTime(time);
		String s = past.getDayOfMonth()+"-"+past.getMonthOfYear()+"-"+past.getYear();
	    return s;
	}
	
	public static String getTimeOfDay(long time) {
		DateTime past=new DateTime(time);
		String s = past.getHourOfDay()+"-"+past.getMinuteOfHour();
	    return s;
	}
	
	public static String getHMinTime(long time) {
		String s = (time/1000/60/60)+"h "+(time/1000/60%60)+"min";
	    return s;
	}
	
	public static int getHours(long time) {
		int i = (int) (time/1000/60/60);
	    return i;
	}
	
	public static int getDaysToXmas(){
		Calendar now = Calendar.getInstance();
		Calendar xmass = Calendar.getInstance();
		xmass.set(Calendar.MONTH, Calendar.DECEMBER);
		xmass.set(Calendar.DAY_OF_MONTH, 24);
		long nowMilli = now.getTimeInMillis();
		long xmassMilli = xmass.getTimeInMillis();
		long delta = xmassMilli - nowMilli;
		int days = (int) Math.abs((delta / 24 / 60 / 60 / 1000));
		return days;
	}
	
	public static int getDaysToHween(){
		Calendar now = Calendar.getInstance();
		Calendar xmass = Calendar.getInstance();
		xmass.set(Calendar.MONTH, Calendar.OCTOBER);
		xmass.set(Calendar.DAY_OF_MONTH, 31);
		long nowMilli = now.getTimeInMillis();
		long xmassMilli = xmass.getTimeInMillis();
		long delta = xmassMilli - nowMilli;
		int days = (int) Math.abs((delta / 24 / 60 / 60 / 1000));
		return days;
	}
	
	public static int getDaysToEaster(){
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
    	Calendar easter = Calendar.getInstance();
    	boolean nineteenthYear = false;
    	int day = (int)(((((((double)(year)/38) - (year/38))*1440) % 60)/2 + 56) % 30)+1;       
    	if((year-5) % 19 == 0){
      		day += 28;
      		nineteenthYear = true;
    	}      
    	easter.set(year,4,day);
    	if(nineteenthYear && easter.get(Calendar.DAY_OF_WEEK) == 7)
    		easter.add(Calendar.DATE, 7);
    	easter.add(Calendar.DATE, -easter.get(Calendar.DAY_OF_WEEK) - 34);
		
		long nowMilli = now.getTimeInMillis();
		long easterMilli = easter.getTimeInMillis();
		long delta = easterMilli - nowMilli;
		int days = (int) Math.abs((delta / 24 / 60 / 60 / 1000));
		return days;
	}

}
