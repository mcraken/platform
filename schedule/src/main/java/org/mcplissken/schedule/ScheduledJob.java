/**
 * 
 */
package org.mcplissken.schedule;

import java.io.Serializable;

import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 10, 2014
 */
public interface ScheduledJob extends Serializable{
	
	public void execute(ScheduleContext context) throws ScheduleException;
}
