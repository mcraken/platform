/**
 * 
 */
package org.cradle.schedule.quartz.builder.calendar;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 8, 2014
 */
public interface CalendarBuilder {
	
	public void build(Scheduler scheduler) throws SchedulerException;
}
