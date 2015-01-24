/**
 * 
 */
package org.mcplissken.schedule.quartz;

import java.util.Date;

import org.mcplissken.schedule.ScheduleContext;
import org.quartz.JobExecutionContext;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 11, 2014
 */
public class QuartzScheduleContext implements ScheduleContext{

	private JobExecutionContext context;
	
	public QuartzScheduleContext(JobExecutionContext context) {
		
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.ScheduleContext#getScheduledTime()
	 */
	@Override
	public Date getScheduledTime() {
		
		return context.getScheduledFireTime();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.ScheduleContext#getNextFireTime()
	 */
	@Override
	public Date getNextFireTime() {
		
		return context.getNextFireTime();
	}

}