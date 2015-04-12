/**
 * 
 */
package org.cradle.schedule;

import java.io.Serializable;

import org.cradle.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 10, 2014
 */
public interface ScheduledJob extends Serializable{
	
	public void execute(ScheduleContext context) throws ScheduleException;
}
