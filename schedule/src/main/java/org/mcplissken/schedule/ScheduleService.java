/**
 * 
 */
package org.mcplissken.schedule;

import java.util.Date;

import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 11, 2014
 */
public interface ScheduleService {
	
	public JobBuildResult buildJob(String jobName, String scheduleName, ScheduledJob job, boolean replace) throws ScheduleException;
	
	public void removeJob(String jobName, String scheduleName) throws ScheduleException;
	
	public boolean taskExists(String jobName, String scheduleName) throws ScheduleException;
	
	public Date getFireTime(String jobName, String scheduleName) throws ScheduleException;
}