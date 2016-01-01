package com.hallym.time;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CurrentTime {
	Calendar cal;
	
	public CurrentTime() {
		// TODO Auto-generated constructor stub
		cal = new GregorianCalendar();
	}
	
	public String currentTime()
	{
		String str = "";
		str = "NOW : " + String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1
				,cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		return str;
	}
}
