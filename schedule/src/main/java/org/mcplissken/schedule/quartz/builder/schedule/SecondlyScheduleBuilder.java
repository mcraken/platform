/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.schedule;

import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForTotalCount;

import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 29, 2014
 */
public class SecondlyScheduleBuilder extends BasicScheduleBuilder {

	private SimpleScheduleBuilder scheduleBuilder;

	/**
	 * 
	 */
	public SecondlyScheduleBuilder(int interval) {
		
		scheduleBuilder = repeatSecondlyForever(interval);
	}
	
	public SecondlyScheduleBuilder(int count, int interval){
		
		scheduleBuilder = repeatSecondlyForTotalCount(count, interval);
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.quartz.builder.QuartzScheduleBuilder#ignoreMisfies()
	 */
	@Override
	public void ignoreMisfires() {
		
		scheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.quartz.builder.BasicScheduleBuilder#getScheduleBuilder()
	 */
	@Override
	protected ScheduleBuilder<?> getScheduleBuilder() {
		
		return scheduleBuilder;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.quartz.builder.schedule.QuartzScheduleBuilder#runOnceOnMisfire()
	 */
	@Override
	public void runOnceOnMisfire() {
		
		scheduleBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
		
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.quartz.builder.schedule.QuartzScheduleBuilder#inTimeZone(java.lang.String)
	 */
	@Override
	public void inTimeZone(String timeZone) {
		
		throw new UnsupportedOperationException();
	}

}
