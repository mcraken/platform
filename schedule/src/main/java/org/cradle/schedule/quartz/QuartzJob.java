/**
 * 
 */
package org.cradle.schedule.quartz;

import org.cradle.schedule.ScheduledJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 11, 2014
 */
public class QuartzJob implements Job{

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		try {
			
			ScheduledJob scheduledJob = getScheduledJob(context);
			
			if(scheduledJob != null) {

				QuartzScheduleContext scheduleContext = new QuartzScheduleContext(context);

				scheduledJob.execute(scheduleContext);
			}

		} catch (Exception e) {

			throw new JobExecutionException(e);
		}
	}

	private ScheduledJob getScheduledJob(JobExecutionContext context)
			throws SchedulerException {

		SchedulerContext schedulerContext = context.getScheduler().getContext();

		String registeredJobName = getRegisteredName(context);

		ScheduledJob scheduledJob =  (ScheduledJob) schedulerContext.get(registeredJobName);

		return scheduledJob;
	}

	private String getRegisteredName(JobExecutionContext context) {

		JobKey jobKey = context.getJobDetail().getKey();

		String registeredJobName = jobKey.getName() + "." + jobKey.getGroup();

		return registeredJobName;
	}

}