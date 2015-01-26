/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.schedule;

import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 29, 2014
 */
public abstract class BasicScheduleBuilder implements QuartzScheduleBuilder {

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.quartz.builder.QuartzScheduleBuilder#schedule(org.quartz.TriggerBuilder)
	 */
	@Override
	public void schedule(TriggerBuilder<Trigger> jobTriggerBuilder) {
		
		jobTriggerBuilder.withSchedule(getScheduleBuilder());
	}
	
	
	protected abstract ScheduleBuilder<?> getScheduleBuilder();
	
}
