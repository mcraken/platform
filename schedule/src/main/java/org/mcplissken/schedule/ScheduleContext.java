/**
 * 
 */
package org.mcplissken.schedule;

import java.util.Date;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 11, 2014
 */
public interface ScheduleContext {
	
	public Date getScheduledTime();
	
	public Date getNextFireTime();
}
