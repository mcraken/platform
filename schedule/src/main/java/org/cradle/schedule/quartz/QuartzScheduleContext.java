/**
 * 
 */
package org.cradle.schedule.quartz;

import java.util.Date;

import org.cradle.schedule.ScheduleContext;
import org.quartz.JobExecutionContext;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 11, 2014
 */
public class QuartzScheduleContext implements ScheduleContext{

	private JobExecutionContext context;
	
	public QuartzScheduleContext(JobExecutionContext context) {
		
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.ScheduleContext#getScheduledTime()
	 */
	@Override
	public Date getScheduledTime() {
		
		return context.getScheduledFireTime();
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.ScheduleContext#getNextFireTime()
	 */
	@Override
	public Date getNextFireTime() {
		
		return context.getNextFireTime();
	}

}