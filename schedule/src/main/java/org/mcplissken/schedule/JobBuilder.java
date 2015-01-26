/**
 * 
 */
package org.mcplissken.schedule;

import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 11, 2014
 */
public interface JobBuilder {
	
	public void scheulde() throws ScheduleException;
	
	public void scheulde(String calendarName) throws ScheduleException;

	public JobBuilder secondly(int interval);
	
	public JobBuilder secondly(int count, int interval);
	
	public JobBuilder daily(int hour, int minute);
	
	public JobBuilder weekly(int day, int hour, int minute);
	
	public JobBuilder ignoreMisfires();
	
	public JobBuilder runOnceOnMisfire();
	
	public JobBuilder inTimeZone(String timeZone);
}
