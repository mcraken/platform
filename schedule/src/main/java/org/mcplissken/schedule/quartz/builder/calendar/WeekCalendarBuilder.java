/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.calendar;


import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.impl.calendar.WeeklyCalendar;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 8, 2014
 */
public class WeekCalendarBuilder extends BasicCalendarBuilder {

	private boolean exclude;

	private int[] days;


	/**
	 * @param exclude the exclude to set
	 */
	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(int[] days) {
		this.days = days;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.quartz.builder.calendar.BasicCalendarBuilder#buildCalendar()
	 */
	@Override
	protected BaseCalendar buildCalendar() {
		
		WeeklyCalendar cal = new WeeklyCalendar();

		for(int day = java.util.Calendar.SUNDAY; day <= java.util.Calendar.SATURDAY; day++){

			cal.setDayExcluded(day, !exclude);
		}

		for(int day : days){

			cal.setDayExcluded(day, exclude);
		}
		
		return cal;
	}
}
