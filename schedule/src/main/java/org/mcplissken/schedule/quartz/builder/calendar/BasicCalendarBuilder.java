/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.calendar;

import java.util.TimeZone;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.calendar.BaseCalendar;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 8, 2014
 */
public abstract class BasicCalendarBuilder implements CalendarBuilder{
	
	private String name;
	
	private String timeZone;
	
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.quartz.builder.calendar.CalendarBuilder#build(org.quartz.Scheduler)
	 */
	@Override
	public void build(Scheduler scheduler) throws SchedulerException {
		
		BaseCalendar cal = buildCalendar();
		
		cal.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		scheduler.addCalendar(name, cal, true, true);
	}
	
	/**
	 * @return
	 */
	protected abstract BaseCalendar buildCalendar();
	
	
}
