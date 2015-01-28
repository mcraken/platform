/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mcplissken.schedule.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.mcplissken.datetime.DateTimeOperation;
import org.mcplissken.datetime.dateback.DateBackStrategy;
import org.mcplissken.datetime.dateback.WeeksDateBackStrategy;
import org.mcplissken.datetime.dateback.WindowDateBackStrategy;
import org.mcplissken.schedule.JobBuilder;
import org.mcplissken.schedule.ScheduledJob;
import org.mcplissken.schedule.exception.ScheduleException;
import org.mcplissken.schedule.quartz.builder.schedule.DailyScheduleBuilder;
import org.mcplissken.schedule.quartz.builder.schedule.QuartzScheduleBuilder;
import org.mcplissken.schedule.quartz.builder.schedule.SecondlyScheduleBuilder;
import org.mcplissken.schedule.quartz.builder.schedule.WeeklyScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 11, 2014
 */
public class QuartzJobBuilder implements JobBuilder {

	private TriggerBuilder<Trigger> jobTriggerBuilder;

	private JobDetail jobDetail;

	private QuartzScheduleService scheduler;

	private String jobName;

	private String scheduleName;

	private ScheduledJob job;

	private QuartzScheduleBuilder scheduleBuilder;

	/**
	 * @throws ScheduleException 
	 * 
	 */
	public QuartzJobBuilder(String jobName, String scheduleName, ScheduledJob job, QuartzScheduleService scheduler) throws ScheduleException {

		jobDetail = newJob(QuartzJob.class).withIdentity(jobName, scheduleName).storeDurably().build();

		jobTriggerBuilder = newTrigger().withIdentity(jobName, scheduleName);

		this.scheduler = scheduler;

		this.jobName = jobName;

		this.scheduleName = scheduleName;

		this.job = job;


	}


	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.ScheduleBuilder#scheulde()
	 */
	@Override
	public void scheulde() throws ScheduleException {

		try {

			scheduleBuilder.schedule(jobTriggerBuilder);

			buildAndScheduleTrigger();

		} catch (SchedulerException e) {

			throw new ScheduleException(e);

		}
	}


	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.JobBuilder#scheulde(java.lang.String)
	 */
	@Override
	public void scheulde(String calendarName) throws ScheduleException {
		
		try {

			scheduleBuilder.schedule(jobTriggerBuilder);
			
			jobTriggerBuilder.modifiedByCalendar(calendarName);
			
			buildAndScheduleTrigger();

		} catch (SchedulerException e) {

			throw new ScheduleException(e);

		}
	}


	private void buildAndScheduleTrigger() throws SchedulerException {
		Trigger trigger = jobTriggerBuilder.build();

		scheduler.schedule(jobDetail, trigger);

		scheduler.registerScheduledJob(jobName, scheduleName, job);
	}

	private void shiftToStart(DateBackStrategy dateBack) {

		DateTimeOperation start = new DateTimeOperation();

		start = dateBack.shift(start);

		jobTriggerBuilder.startAt(start.getDate());
	}

	private SecondlyScheduleBuilder shiftToMinuteStart(int interval) {

		shiftToStart(new WindowDateBackStrategy(1));

		return new SecondlyScheduleBuilder(interval);
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.JobBuilder#secondly(int)
	 */
	@Override
	public JobBuilder secondly(int interval) {

		scheduleBuilder = shiftToMinuteStart(interval);
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.JobBuilder#secondly(int, int)
	 */
	@Override
	public JobBuilder secondly(int count, int interval) {
		
		jobTriggerBuilder.startNow();
		
		scheduleBuilder = new SecondlyScheduleBuilder(count, interval);
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.ScheduleBuilder#daily()
	 */
	@Override
	public JobBuilder daily(int hour, int minute) {

		jobTriggerBuilder.startNow();

		scheduleBuilder = new DailyScheduleBuilder(hour, minute);

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.JobBuilder#weekly(int, int, int)
	 */
	@Override
	public JobBuilder weekly(int day, int hour, int minute) {

		DateTimeOperation start = new DateTimeOperation();

		new WeeksDateBackStrategy()
		.shift(start)
		.plusDays(day - 1)
		.plusHours(hour)
		.plusMinutes(minute);

		jobTriggerBuilder.startAt(start.getDate());

		day = day + 1 < 7 ? ++day : 1;

		scheduleBuilder = new WeeklyScheduleBuilder(day, hour, minute);

		return this;
	}


	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.JobBuilder#ignoreMisfires()
	 */
	@Override
	public JobBuilder ignoreMisfires() {

		scheduleBuilder.ignoreMisfires();

		return this;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.JobBuilder#runOnceOnMisfire()
	 */
	@Override
	public JobBuilder runOnceOnMisfire() {

		scheduleBuilder.runOnceOnMisfire();

		return this;
	}


	/* (non-Javadoc)
	 * @see org.mcplissken.schedule.JobBuilder#inTimeZone()
	 */
	@Override
	public JobBuilder inTimeZone(String timeZone) {
		
		scheduleBuilder.inTimeZone(timeZone);
		
		return this;
	}

}
