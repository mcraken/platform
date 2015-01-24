/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.schedule;

import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 29, 2014
 */
public abstract class BasicScheduleBuilder implements QuartzScheduleBuilder {

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.quartz.builder.QuartzScheduleBuilder#schedule(org.quartz.TriggerBuilder)
	 */
	@Override
	public void schedule(TriggerBuilder<Trigger> jobTriggerBuilder) {
		
		jobTriggerBuilder.withSchedule(getScheduleBuilder());
	}
	
	
	protected abstract ScheduleBuilder<?> getScheduleBuilder();
	
}
