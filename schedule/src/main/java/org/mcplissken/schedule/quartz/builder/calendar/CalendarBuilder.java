/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.calendar;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 8, 2014
 */
public interface CalendarBuilder {
	
	public void build(Scheduler scheduler) throws SchedulerException;
}
