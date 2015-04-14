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
package org.cradle.schedule.quartz;

import java.util.Date;

import org.cradle.schedule.JobBuildResult;
import org.cradle.schedule.ScheduleService;
import org.cradle.schedule.ScheduledJob;
import org.cradle.schedule.exception.ScheduleException;
import org.cradle.schedule.quartz.builder.calendar.CalendarBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 17, 2014
 */
public class QuartzScheduleService implements ScheduleService{

	private SchedulerFactory schedulerFactory;

	private Scheduler scheduler;

	private String quartzFile;

	private CalendarBuilder[] calendarBuilders;

	/**
	 * @param calendarBuilders the calendarBuilders to set
	 */
	public void setCalendarBuilders(CalendarBuilder[] calendarBuilders) {
		this.calendarBuilders = calendarBuilders;
	}

	/**
	 * @param quartzFile the quartzFile to set
	 */
	public void setQuartzFile(String quartzFile) {
		this.quartzFile = quartzFile;
	}

	public void init() throws Exception{

		schedulerFactory = new StdSchedulerFactory(quartzFile);

		scheduler = schedulerFactory.getScheduler();

		scheduler.start();

		buildCalendars();
	}

	private void buildCalendars() throws SchedulerException {

		if(calendarBuilders != null){

			for(CalendarBuilder builder : calendarBuilders)
				builder.build(scheduler);
		}
	}

	public void destroy() throws Exception{

		scheduler.shutdown();
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.ScheduleService#addJob(java.lang.String, java.lang.String, org.cradle.schedule.ScheduledJob)
	 */
	@Override
	public JobBuildResult buildJob(String jobName, String scheduleName, ScheduledJob job, boolean replace) throws ScheduleException {

		try {

			JobBuildResult result = new JobBuildResult();

			boolean taskExists = taskExists(jobName, scheduleName);

			if(replace && taskExists){

				removeJob(jobName, scheduleName);

			} else if(taskExists){

				registerScheduledJob(jobName, scheduleName, job);

				result.scheduled = true;

				return result;
			}

			result.builder = createBuilder(jobName, scheduleName, job);

			return result;

		} catch (SchedulerException e) {

			throw new ScheduleException(e);
		}

	}

	private QuartzJobBuilder createBuilder(String jobName, String scheduleName,
			ScheduledJob job) throws ScheduleException {

		return new QuartzJobBuilder(jobName, scheduleName, job, this);
	}


	public void registerScheduledJob(String jobName, String scheduleName,
			ScheduledJob job) throws SchedulerException {

		scheduler.getContext().put(createTaskName(jobName, scheduleName), job);
	}

	public String createTaskName(String jobName, String scheduleName) {

		return jobName + "." + scheduleName;
	}

	public boolean taskExists(String jobName, String scheduleName)
			throws ScheduleException {

		try {
			
			return scheduler.checkExists(new JobKey(jobName, scheduleName));
			
		} catch (SchedulerException e) {
			
			throw new ScheduleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.ScheduleService#removeJob(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeJob(String jobName, String scheduleName) throws ScheduleException {

		try {

			JobKey key = new JobKey(jobName, scheduleName);

			scheduler.deleteJob(key);

		} catch (SchedulerException e) {

			throw new ScheduleException(e);
		}
	}

	public void schedule(JobDetail job, Trigger trigger) throws SchedulerException{

		scheduler.scheduleJob(job, trigger);
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.ScheduleService#getFireTime(java.lang.String, java.lang.String)
	 */
	@Override
	public Date getFireTime(String jobName, String scheduleName) throws ScheduleException {
		
		try {
			
			TriggerKey key = new TriggerKey(jobName, scheduleName);

			Trigger trigger = scheduler.getTrigger(key);
			
			return trigger.getNextFireTime();
			
		} catch (SchedulerException e) {
			
			throw new ScheduleException(e);
		}
		
	}

}