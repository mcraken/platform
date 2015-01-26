/**
 * 
 */
package org.mcplissken.datetime;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 27, 2014
 */
public class DateTimeOperation  {

	private static final int MINUTE_WINDOW_MILLI = 60000;
	public static final String DATE_FORMAT = "yyyyMMdd";
	public static final String DATE_TIME_FORMAT = "yyyyMMdd HHmmss";
	public static final String ZERO_CLOCK = "000000";


	public enum Week {

		SATURDAY("Saturday"), SUNDAY("Sunday");

		public String value;

		private Week(String value) {
			this.value = value;
		}

	};

	private DateTime start;

	public DateTimeOperation(String time){

		this();

		String date = start.toString(DateTimeFormat.forPattern(DATE_FORMAT).withZone(DateTimeZone.UTC));
		
		start = DateTime.parse(date + " " + time, DateTimeFormat.forPattern(DATE_TIME_FORMAT).withZone(DateTimeZone.UTC));
	}

	public DateTimeOperation(String datetime, String pattern){
		
		start = DateTime.parse(datetime, DateTimeFormat.forPattern(pattern).withZone(DateTimeZone.UTC));
	}
	
	public DateTimeOperation() {

		this.start = new DateTime(DateTimeZone.UTC);
	}

	public DateTimeOperation(long mill){

		this.start = new DateTime(mill, DateTimeZone.UTC);
	}

	private DateTimeOperation(DateTime start){
		this.start = start;
	}

	public DateTimeOperation startOfSecond(){

		start = start.millisOfSecond().withMinimumValue();

		return this;
	}

	public DateTimeOperation startOfMinute(){

		start = start.secondOfMinute().withMinimumValue();

		return this;
	}

	public DateTimeOperation startOfHour(){

		start = start.minuteOfHour().withMinimumValue();

		return this;
	}

	public DateTimeOperation startOfDay(){

		start = start.hourOfDay().withMinimumValue();

		return this;
	}

	public DateTimeOperation startOfWeek(){

		start = start.dayOfWeek().withMinimumValue();

		return this;
	}

	public DateTimeOperation startOfMonth(){

		start = start.dayOfMonth().withMinimumValue();

		return this;
	}

	public DateTimeOperation startOfYear(){

		start = start.dayOfYear().withMinimumValue();

		return this;
	}

	public long getMilliseconds(){

		return start.getMillis();
	}

	public DateTimeOperation plusSeconds(int amount){

		start = start.plusSeconds(amount);

		return this;
	}
	
	public DateTimeOperation minusSeconds(int amount){

		start = start.minusSeconds(amount);

		return this;
	}
	
	public DateTimeOperation plusMinutes(int amount){

		start = start.plusMinutes(amount);

		return this;
	}
	
	public DateTimeOperation minusMinutes(int amount){

		start = start.minusMinutes(amount);

		return this;
	}
	
	public DateTimeOperation plusHours(int amount){

		start = start.plusHours(amount);

		return this;
	}
	
	public DateTimeOperation minusHours(int amount){

		start = start.minusHours(amount);

		return this;
	}
	
	public DateTimeOperation plusDays(int amount){

		start = start.plusDays(amount);

		return this;
	}
	
	public DateTimeOperation minusDays(int amount){

		start = start.minusDays(amount);

		return this;
	}

	public DateTimeOperation minusWeeks(int amount){

		start = start.minusWeeks(amount);

		return this;
	}

	public DateTimeOperation minusMonths(int amount){

		start = start.minusMonths(amount);

		return this;
	}

	public DateTimeOperation minusYears(int amount){

		start = start.minusYears(amount);

		return this;
	}

	public DateTimeOperation startOfWindow(int minutes){

		DateTimeOperation refOp = new DateTimeOperation(start);

		long refPoint = refOp.startOfWeek().startOfDay().startOfHour().startOfMinute().startOfSecond().getMilliseconds();

		long startTime = start.getMillis();

		long windowWeight = windowWeight(minutes);

		long targetwindow = startTime - ((startTime - refPoint) % windowWeight);
		
		return updateStart(targetwindow);
	}

	private DateTimeOperation updateStart(long targetwindow) {

		start = new DateTime(targetwindow, DateTimeZone.UTC);

		return this;
	}

	private long windowWeight(int minutes) {

		long windowWeight = minutes * MINUTE_WINDOW_MILLI;

		return windowWeight;
	}

	public DateTimeOperation minusWindow(int minutes, int amount){

		long targetwindow = start.getMillis();

		long windowWeight = windowWeight(minutes);

		targetwindow = targetwindow - (windowWeight * amount);

		return updateStart(targetwindow);
	}

	public Date getDate(){

		return start.toDate();

	}

	public DateTimeOperation shiftWeekend(){

		String dayOfWeek = start.dayOfWeek().getAsText(); 

		if(dayOfWeek.equalsIgnoreCase(Week.SATURDAY.value)){

			start = start.minusDays(2);

		} else if(dayOfWeek.equalsIgnoreCase(Week.SUNDAY.value)){

			start = start.minusDays(3);

		} 

		return this;
	}

	public boolean isWeekend(){

		String dayOfWeek = start.dayOfWeek().getAsText();

		if(
				dayOfWeek.equalsIgnoreCase(Week.SATURDAY.value) 
				|| dayOfWeek.equalsIgnoreCase(Week.SUNDAY.value)
				){

			return true;

		} 

		return false;
	}
	
	public boolean dayEquals(long otherTimiing){
		
		 DateTime otherDate = new DateTime(otherTimiing, DateTimeZone.UTC);
		 
		 if(start.getYear() != otherDate.getYear())
			 return false;
		 
		 if(start.getDayOfYear() != otherDate.getDayOfYear())
			 return false;
		 
		return true;
	}
}
