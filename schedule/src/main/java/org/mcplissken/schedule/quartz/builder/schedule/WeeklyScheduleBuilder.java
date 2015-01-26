/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.schedule;

import static org.quartz.CronScheduleBuilder.weeklyOnDayAndHourAndMinute;

import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.ScheduleBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 29, 2014
 */
public class WeeklyScheduleBuilder extends BasicScheduleBuilder {
	
	
	private CronScheduleBuilder scheduleBuilder;

	/**
	 * 
	 */
	public WeeklyScheduleBuilder(int day, int hour, int minute) {
		
		scheduleBuilder = weeklyOnDayAndHourAndMinute(day, hour, minute);
		
		scheduleBuilder.inTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.quartz.builder.QuartzScheduleBuilder#ignoreMisfies()
	 */
	@Override
	public void ignoreMisfires() {
		
		scheduleBuilder.withMisfireHandlingInstructionDoNothing();
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.quartz.builder.BasicScheduleBuilder#getScheduleBuilder()
	 */
	@Override
	protected ScheduleBuilder<?> getScheduleBuilder() {
		
		return scheduleBuilder;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.quartz.builder.schedule.QuartzScheduleBuilder#runOnceOnMisfire()
	 */
	@Override
	public void runOnceOnMisfire() {
		
		scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
		
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.quartz.builder.schedule.QuartzScheduleBuilder#inTimeZone(java.lang.String)
	 */
	@Override
	public void inTimeZone(String timeZone) {
		scheduleBuilder.inTimeZone(TimeZone.getTimeZone(timeZone));
	}

}
