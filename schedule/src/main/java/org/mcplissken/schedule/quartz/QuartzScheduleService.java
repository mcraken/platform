/**
 * 
 */
package org.mcplissken.schedule.quartz;

import java.util.Date;

import org.mcplissken.schedule.JobBuildResult;
import org.mcplissken.schedule.ScheduleService;
import org.mcplissken.schedule.ScheduledJob;
import org.mcplissken.schedule.exception.ScheduleException;
import org.mcplissken.schedule.quartz.builder.calendar.CalendarBuilder;
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
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.market.schedule.ScheduleService#addJob(java.lang.String, java.lang.String, com.mubasher.market.schedule.ScheduledJob)
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
	 * @see com.mubasher.market.schedule.ScheduleService#removeJob(java.lang.String, java.lang.String)
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
	 * @see com.mubasher.market.schedule.ScheduleService#getFireTime(java.lang.String, java.lang.String)
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