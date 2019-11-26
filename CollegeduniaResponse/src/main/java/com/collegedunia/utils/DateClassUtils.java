package com.collegedunia.utils;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateClassUtils {
	static GregorianCalendar date = new GregorianCalendar();
	public static final int DAY = date.get(Calendar.DATE);
	public static final int MONTH =  date.get(Calendar.MONTH) + 1;
	public static final int YEAR = date.get(Calendar.YEAR);
	
	public String getCurrentDate() {  
		 //Get current date time
        LocalDateTime now = LocalDateTime.now();  
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm"); 
        String date = now.format(formatter);
		return date;
	}
	
	public String getCurrentDateTime() {  
		 //Get current date time
       LocalDateTime now = LocalDateTime.now();  
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
       String date = now.format(formatter);
		return date;
	}

	public String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	} 
	public static void main(String[] args) {
		DateClassUtils dcu = new DateClassUtils();
		String date = dcu.getCurrentDateTime();
		System.out.println(date);
	}

}
