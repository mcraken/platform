/**
 * 
 */
package org.mcplissken.schedule;

import java.util.Date;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 11, 2014
 */
public interface ScheduleContext {
	
	public Date getScheduledTime();
	
	public Date getNextFireTime();
}
